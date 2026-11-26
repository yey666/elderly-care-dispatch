package com.neusoft.elderlycaredispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("service_request")
public class ServiceRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long elderlyId;
    private String serviceType;
    private LocalDateTime expectTime;
    private String area;
    private String address;
    private Integer urgency; // 1普通 2较急 3紧急
    private String status; // PENDING待派单...
    private String remark;
    private Long createBy;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}