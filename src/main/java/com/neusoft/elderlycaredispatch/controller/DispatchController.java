package com.neusoft.elderlycaredispatch.controller;

import com.neusoft.elderlycaredispatch.common.Result;
import com.neusoft.elderlycaredispatch.entity.Worker;
import com.neusoft.elderlycaredispatch.service.DispatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "智能派单")
@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    @Operation(summary = "获取派单候选人（加权打分排序）")
    @GetMapping("/candidates/{requestId}")
    public Result<List<Worker>> getCandidates(@PathVariable Long requestId) {
        return Result.success(dispatchService.getCandidates(requestId));
    }

    @Operation(summary = "确认执行派单（多表事务）")
    @PostMapping("/execute")
    public Result<String> execute(@RequestParam Long requestId, @RequestParam Long workerId) {
        dispatchService.executeDispatch(requestId, workerId);
        return Result.success("派单成功");
    }

    @Operation(summary = "服务人员接单")
    @PostMapping("/accept/{dispatchId}")
    public Result<String> accept(@PathVariable Long dispatchId) {
        dispatchService.acceptDispatch(dispatchId);
        return Result.success("接单成功");
    }

    @Operation(summary = "服务人员完成服务")
    @PostMapping("/finish/{dispatchId}")
    public Result<String> finish(@PathVariable Long dispatchId) {
        dispatchService.finishDispatch(dispatchId);
        return Result.success("服务已完成");
    }
}