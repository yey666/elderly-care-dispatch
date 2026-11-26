package com.neusoft.elderlycaredispatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.elderlycaredispatch.common.BizException;
import com.neusoft.elderlycaredispatch.entity.DispatchRecord;
import com.neusoft.elderlycaredispatch.entity.ServiceRequest;
import com.neusoft.elderlycaredispatch.entity.Worker;
import com.neusoft.elderlycaredispatch.mapper.WorkerMapper;
import com.neusoft.elderlycaredispatch.service.DispatchRecordService;
import com.neusoft.elderlycaredispatch.service.DispatchService;
import com.neusoft.elderlycaredispatch.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private ServiceRequestService requestService;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private DispatchRecordService dispatchRecordService;

    @Override
    public List<Worker> getCandidates(Long requestId) {
        // 1. 获取请求详情
        ServiceRequest request = requestService.getById(requestId);
        if (request == null) {
            throw new BizException("服务请求不存在");
        }
        if (!"PENDING".equals(request.getStatus())) {
            throw new BizException("该请求不在待派单状态");
        }

        // 2. 硬筛选：查所有可用的工人
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getStatus, 1)
                .lt(Worker::getCurrentLoad, 5); // 简易：假设最大负载是5

        List<Worker> workers = workerMapper.selectList(wrapper);

        // 3. 过滤：技能和区域必须匹配
        List<Worker> candidates = workers.stream()
                .filter(w -> w.getSkillTags().contains(request.getServiceType()))
                .filter(w -> w.getServiceAreas().contains(request.getArea()))
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            throw new BizException("当前区域无可用服务人员");
        }

        // 4. 按分数降序排序
        candidates.sort((w1, w2) -> {
            double s1 = calcScore(w1, request);
            double s2 = calcScore(w2, request);
            return Double.compare(s2, s1);
        });

        return candidates;
    }

    // 核心打分逻辑
    private double calcScore(Worker w, ServiceRequest r) {
        // 负载得分：负载越少，得分越高
        double loadScore = 1.0 - (w.getCurrentLoad() / 5.0);
        if (loadScore < 0) loadScore = 0;

        // 区域得分：这里简化处理，过了硬筛选就是1.0
        double areaScore = 1.0;

        // 紧急程度加权
        double urgencyBonus = 0;
        if (r.getUrgency() == 3 && loadScore > 0.6) {
            urgencyBonus = 0.15;
        }

        // 加权求和：负载占45%，区域占35%，紧急加分占20%
        return 0.45 * loadScore + 0.35 * areaScore + 0.20 * urgencyBonus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeDispatch(Long requestId, Long workerId) {
        // 1. 校验请求状态
        ServiceRequest request = requestService.getById(requestId);
        if (request == null) {
            throw new BizException("服务请求不存在");
        }
        if (!"PENDING".equals(request.getStatus())) {
            throw new BizException("该请求已被派单或已取消，请勿重复操作");
        }

        // 2. 校验工人状态
        Worker worker = workerMapper.selectById(workerId);
        if (worker == null || worker.getStatus() != 1) {
            throw new BizException("该服务人员当前不可用");
        }
        if (worker.getCurrentLoad() >= worker.getMaxLoad()) {
            throw new BizException("该服务人员负载已满");
        }

        // 3. 插入派单记录
        DispatchRecord record = new DispatchRecord();
        record.setRequestId(requestId);
        record.setWorkerId(workerId);
        record.setDispatchType(2); // 2表示管理员手动确认派单
        record.setStatus("DISPATCHED");
        record.setDispatchTime(LocalDateTime.now());
        dispatchRecordService.save(record);

        // 4. 更新请求状态
        request.setStatus("DISPATCHED");
        requestService.updateById(request);

        // 5. 更新工人负载
        worker.setCurrentLoad(worker.getCurrentLoad() + 1);
        workerMapper.updateById(worker);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptDispatch(Long dispatchId) {
        // 1. 查派单记录并校验
        DispatchRecord record = dispatchRecordService.getById(dispatchId);
        if (record == null) {
            throw new BizException("派单记录不存在");
        }
        if (!"DISPATCHED".equals(record.getStatus())) {
            throw new BizException("当前派单状态不允许接单");
        }

        // 2. 更新派单记录状态
        record.setStatus("ACCEPTED");
        record.setAcceptTime(LocalDateTime.now());
        dispatchRecordService.updateById(record);

        // 3. 更新服务请求状态
        ServiceRequest request = requestService.getById(record.getRequestId());
        request.setStatus("ACCEPTED");
        requestService.updateById(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishDispatch(Long dispatchId) {
        // 1. 查派单记录并校验
        DispatchRecord record = dispatchRecordService.getById(dispatchId);
        if (record == null) {
            throw new BizException("派单记录不存在");
        }
        if (!"ACCEPTED".equals(record.getStatus())) {
            throw new BizException("只有已接单的工单才能完成");
        }

        // 2. 更新派单记录状态
        record.setStatus("COMPLETED");
        record.setFinishTime(LocalDateTime.now());
        dispatchRecordService.updateById(record);

        // 3. 更新服务请求状态
        ServiceRequest request = requestService.getById(record.getRequestId());
        request.setStatus("COMPLETED");
        requestService.updateById(request);

        // 4. 扣减服务人员负载
        Worker worker = workerMapper.selectById(record.getWorkerId());
        if (worker != null && worker.getCurrentLoad() > 0) {
            worker.setCurrentLoad(worker.getCurrentLoad() - 1);
            workerMapper.updateById(worker);
        }
    }
}