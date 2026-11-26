package com.neusoft.elderlycaredispatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dispatch_record")
public class DispatchRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long requestId;
    private Long workerId;
    private Integer dispatchType; // 1系统加权 2管理员手动
    private String scoreDetail; // 打分明细
    private String status; // DISPATCHED, ACCEPTED...
    private LocalDateTime dispatchTime;
    private LocalDateTime acceptTime;
    private LocalDateTime finishTime;
    private String proofUrl;
    private String rejectReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer version; // 乐观锁
}