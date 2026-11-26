package com.neusoft.elderlycaredispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.elderlycaredispatch.entity.Elderly;
import com.neusoft.elderlycaredispatch.mapper.ElderlyMapper;
import com.neusoft.elderlycaredispatch.service.ElderlyService;
import org.springframework.stereotype.Service;

@Service
public class ElderlyServiceImpl extends ServiceImpl<ElderlyMapper, Elderly> implements ElderlyService {
}