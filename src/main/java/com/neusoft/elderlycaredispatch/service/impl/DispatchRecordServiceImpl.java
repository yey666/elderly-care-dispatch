package com.neusoft.elderlycaredispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.elderlycaredispatch.entity.DispatchRecord;
import com.neusoft.elderlycaredispatch.mapper.DispatchRecordMapper;
import com.neusoft.elderlycaredispatch.service.DispatchRecordService;
import org.springframework.stereotype.Service;

@Service
public class DispatchRecordServiceImpl extends ServiceImpl<DispatchRecordMapper, DispatchRecord> implements DispatchRecordService {
}