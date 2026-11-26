package com.neusoft.elderlycaredispatch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.elderlycaredispatch.common.Result;
import com.neusoft.elderlycaredispatch.entity.Worker;
import com.neusoft.elderlycaredispatch.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "服务人员管理")
@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Operation(summary = "分页条件查询服务人员")
    @GetMapping("/page")
    public Result<Page<Worker>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String skillTags) {

        Page<Worker> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        // 模糊搜索名字和技能
        wrapper.like(name != null && !name.isEmpty(), Worker::getName, name)
                .like(skillTags != null && !skillTags.isEmpty(), Worker::getSkillTags, skillTags)
                .orderByDesc(Worker::getCreateTime);

        return Result.success(workerService.page(page, wrapper));
    }

    @Operation(summary = "新增服务人员")
    @PostMapping
    public Result<String> add(@RequestBody Worker worker) {
        worker.setCurrentLoad(0); // 默认负载为0
        worker.setStatus(1); // 默认可用
        workerService.save(worker);
        return Result.success("新增成功");
    }

    @Operation(summary = "修改服务人员")
    @PutMapping
    public Result<String> update(@RequestBody Worker worker) {
        workerService.updateById(worker);
        return Result.success("修改成功");
    }

    @Operation(summary = "删除服务人员")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        workerService.removeById(id);
        return Result.success("删除成功");
    }
}