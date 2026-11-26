package com.neusoft.elderlycaredispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("elderly")
public class Elderly {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer gender; // 0未知 1男 2女
    private Integer age;
    private String phone;
    private String area;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private String diseaseTags;
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}