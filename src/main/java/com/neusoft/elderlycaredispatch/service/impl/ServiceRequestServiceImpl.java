package com.neusoft.elderlycaredispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.elderlycaredispatch.entity.ServiceRequest;
import com.neusoft.elderlycaredispatch.mapper.ServiceRequestMapper;
import com.neusoft.elderlycaredispatch.service.ServiceRequestService;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestServiceImpl extends ServiceImpl<ServiceRequestMapper, ServiceRequest> implements ServiceRequestService {
}