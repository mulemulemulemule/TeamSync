package com.mule.demo.controller;

import com.mule.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mule.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.mule.demo.model.dto.TaskCreateDTO;

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

}
