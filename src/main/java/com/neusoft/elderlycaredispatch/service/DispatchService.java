package com.neusoft.elderlycaredispatch.service;

import com.neusoft.elderlycaredispatch.entity.Worker;
import java.util.List;

public interface DispatchService {
    // 获取智能派单的候选人列表（带打分）
    List<Worker> getCandidates(Long requestId);

    // 执行派单（核心事务方法）
    void executeDispatch(Long requestId, Long workerId);

    // 服务人员接单
    void acceptDispatch(Long dispatchId);

    // 服务人员完成服务
    void finishDispatch(Long dispatchId);
}