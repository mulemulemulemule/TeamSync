package com.mule.demo.controller;

import com.mule.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mule.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.mule.demo.model.dto.TaskCreateDTO;
import java.util.List;
import java.util.Map;
import com.mule.demo.entity.Task;
import com.mule.demo.model.dto.TaskUpdateDTO;

@Tag(name="任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary ="创建任务")
    @PostMapping("/create")
    public Result<String> create(@RequestBody @Valid TaskCreateDTO dto) {
taskService.createTask(dto);
        return Result.success("任务创建成功");
    }

    @Operation(summary ="获取任务看板")
    @GetMapping("/board/{projectId}")
    public Result<Map<Integer,List<Task>>> getTaskBoard(@PathVariable Long projectId) {
        return Result.success(taskService.getTaskBoard(projectId));
    }
    @Operation(summary ="更新任务")
    @PostMapping("/update")
    public Result<String> updateTask(@RequestBody @Valid TaskUpdateDTO dto) {
        taskService.updateTask(dto);
        return Result.success("update success");
    }
}
