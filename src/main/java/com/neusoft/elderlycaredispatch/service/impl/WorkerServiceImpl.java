package com.neusoft.elderlycaredispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.elderlycaredispatch.entity.Worker;
import com.neusoft.elderlycaredispatch.mapper.WorkerMapper;
import com.neusoft.elderlycaredispatch.service.WorkerService;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements WorkerService {
}