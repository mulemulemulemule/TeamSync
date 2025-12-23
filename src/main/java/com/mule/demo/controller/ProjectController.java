package com.mule.demo.controller;

import com.mule.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mule.demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.mule.demo.model.dto.ProjectCreateDTO;

@Tag(name="项目管理")
@RestController
@RequestMapping("/project")
public class ProjectController {

@Autowired
private ProjectService projectService;

@Operation(summary ="创建项目")
@PostMapping("/create")
public Result<String> create(@RequestBody @Valid ProjectCreateDTO dto){
    projectService.createProject(dto);
return Result.success("创建成功");
}
}
