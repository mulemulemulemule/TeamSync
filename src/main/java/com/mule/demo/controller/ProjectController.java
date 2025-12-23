package com.mule.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mule.demo.common.Result;
import com.mule.demo.common.UserContext;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="项目管理")
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary ="创建项目")
    @PostMapping("/create")
    public Result<String> create(@RequestBody @Valid ProjectCreateDTO dto){
        dto.setOwnerId(UserContext.getUserId());
        projectService.createProject(dto);
        return Result.success("创建成功");
    }

    @Operation(summary = "获取我的项目列表")
    @GetMapping("/list")
    public Result<List<Project>> listMyProjects() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getOwnerId, userId).orderByDesc(Project::getCreateTime);
        return Result.success(projectService.list(wrapper));
    }
}