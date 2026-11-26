package com.neusoft.elderlycaredispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 服务人员实体类
 */
@Data
@TableName("worker")
public class Worker {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String phone;
    private String skillTags; // 技能标签，逗号分隔
    private String serviceAreas; // 负责区域，逗号分隔
    private Integer maxLoad; // 最大接单数
    private Integer currentLoad; // 当前负载数
    private Integer status; // 1可用 0停用 2忙碌

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableLogic // 开启逻辑删除
    private Integer deleted;
}