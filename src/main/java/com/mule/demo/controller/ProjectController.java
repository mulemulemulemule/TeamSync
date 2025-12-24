package com.mule.demo.controller;

import com.mule.demo.common.Result;
import com.mule.demo.common.UserContext;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.dto.InviteHandleDTO;
import com.mule.demo.service.ProjectService;
import com.mule.demo.model.vo.ProjectMemberVO;

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
        return Result.success("create project success");
    }
    @Operation(summary ="获取项目成员列表")
    @GetMapping("/members/{projectId}")
    public Result<List<ProjectMemberVO>> listMembers(@PathVariable Long projectId) {
        return Result.success(projectService.listMembers(projectId));
    }
    @Operation(summary = "获取我参与的项目列表")
    @GetMapping("/list")
    public Result<List<Project>> listMyProjects() {
        Long userId = UserContext.getUserId();
List<Project> projects = projectService.listMyProjects(userId);
        return Result.success(projects);
    }
    @Operation(summary = "获取公开项目列表")
    @GetMapping("/hall")
    public Result<List<Project>> listPublicProjects() {
        List<Project> projects = projectService.listPublicProjects();
        return Result.success(projects);
    }
    @Operation(summary = "获取待处理邀请列表")
    @GetMapping("/invite/list")
    public Result<List<Project>> listPendingInvites() {
        return Result.success(projectService.listPendingInvites(UserContext.getUserId()));
    }
    @Operation(summary = "邀请成员")
    @PostMapping("/invite")
    public Result<String> inviteMember(@RequestBody @Valid ProjectInviteDTO inviteDTO) {
        Long currentUserId= UserContext.getUserId();
        projectService.inviteMember( currentUserId,inviteDTO);
        return Result.success("invite success");

        
    }
    @Operation(summary = "处理邀请")
    @PostMapping("/invite/handle")
    public Result<String> handleInvite(@RequestBody @Valid InviteHandleDTO dto) {
        Long currentUserId = UserContext.getUserId();
         projectService.handleInvite(currentUserId, dto);
         String msg = dto.getAccept() ? "success invite" : "refuse invite";
         return Result.success(msg);
    }
}