package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.entity.Project;
import com.mule.demo.entity.ProjectMember;
import com.mule.demo.entity.User;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.ProjectMapper;
import com.mule.demo.mapper.ProjectMemberMapper;
import com.mule.demo.mapper.UserMapper;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.dto.InviteHandleDTO;
import com.mule.demo.model.vo.ProjectMemberVO;
import com.mule.demo.service.RabbitMQService;
import com.mule.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ProjectMemberMapper projectMemberMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RabbitMQService rabbitMQService;

    @Override
    @Transactional
    public void createProject(ProjectCreateDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwnerId(dto.getOwnerId());
        project.setType(dto.getType());
        this.save(project);

        ProjectMember member = new ProjectMember();
        member.setProjectId(project.getId());
        member.setUserId(dto.getOwnerId());
        member.setRole("OWNER");
        member.setStatus(1);
        projectMemberMapper.insert(member);
    }

    @Override
    public List<Project> listMyProjects(Long userId) {
        List<Long> ids = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 1);
        return ids.isEmpty() ? Collections.emptyList() : this.list(new LambdaQueryWrapper<Project>().in(Project::getId, ids).orderByDesc(Project::getCreateTime));
    }

    @Override
    public List<Project> listPendingInvites(Long userId) {
        List<Long> ids = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 0);
        return ids.isEmpty() ? Collections.emptyList() : this.list(new LambdaQueryWrapper<Project>().in(Project::getId, ids).orderByDesc(Project::getCreateTime));
    }

    @Override
    public List<Project> listPublicProjects() {
        return this.list(new LambdaQueryWrapper<Project>().eq(Project::getType, 1).orderByDesc(Project::getCreateTime));
    }

    @Override
    public void inviteMember(Long currentUserId, ProjectInviteDTO dto) {
        Project p = this.getById(dto.getProjectId());
        if (p == null || !p.getOwnerId().equals(currentUserId)) throw new ServiceException(403, "Forbidden");
        User target = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (target == null) throw new ServiceException("User not found");
        
        ProjectMember member = new ProjectMember();
        member.setProjectId(p.getId());
        member.setUserId(target.getId());
        member.setRole("MEMBER");
        member.setStatus(0);
        projectMemberMapper.insert(member);

        rabbitMQService.sendInviteNotify(p.getId(), p.getName(), target.getId());
    }

    @Override
    public void handleInvite(Long currentUserId, InviteHandleDTO dto) {
        ProjectMember m = projectMemberMapper.selectOne(new LambdaQueryWrapper<ProjectMember>().eq(ProjectMember::getProjectId, dto.getProjectId()).eq(ProjectMember::getUserId, currentUserId).eq(ProjectMember::getStatus, 0));
        if (m == null) throw new ServiceException("Invite not found");
        if (dto.getAccept()) { m.setStatus(1); projectMemberMapper.updateById(m); }
        else projectMemberMapper.deleteById(m.getId());
    }

    @Override
    public List<ProjectMemberVO> listMembers(Long projectId) {
        return projectMemberMapper.listProjectMembers(projectId);
    }
}
