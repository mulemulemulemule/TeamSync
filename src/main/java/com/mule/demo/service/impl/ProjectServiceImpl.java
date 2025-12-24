package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.config.RabbitConfig;
import com.mule.demo.entity.Project;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.ProjectMapper;
import com.mule.demo.mapper.ProjectMemberMapper;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.service.ProjectService;
import java.util.Collections;
import java.util.List;
import com.mule.demo.mapper.UserMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mule.demo.entity.ProjectMember;
import com.mule.demo.entity.User;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.dto.InviteHandleDTO;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService{

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

@Override
@Transactional
    public void createProject(ProjectCreateDTO dto) {

LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.eq(Project::getName,dto.getName()).eq(Project::getOwnerId,dto.getOwnerId());
      
        if (this.count(queryWrapper)>0) {
            throw new ServiceException("Project ... is already taken");
        }

        Project project = new Project();

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwnerId(dto.getOwnerId());
        project.setType(dto.getType());
        this.save(project);


        ProjectMember projectMember = new ProjectMember();

        projectMember.setProjectId(project.getId());
        projectMember.setUserId(dto.getOwnerId());
        projectMember.setRole("OWNER");
        projectMember.setStatus(1);
        projectMemberMapper.insert(projectMember);
    }


    @Override
    public List<Project> listMyProjects(Long userId){

List<Long> projectIds = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 1);

if(projectIds.isEmpty()||projectIds==null){

    return Collections.emptyList();

}


LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();

queryWrapper.in(Project::getId,projectIds).orderByDesc(Project::getCreateTime);

return this.list(queryWrapper);
    }


    @Override
    public List<Project> listPublicProjects(){
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getType,1).orderByDesc(Project::getCreateTime);
        return this.list(queryWrapper);
    }

    
    @Override
    public List<Project> listPendingInvites(Long userId){
        List<Long> projectIds = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 0);

if(projectIds.isEmpty()||projectIds==null){

    return Collections.emptyList();

}

LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();

queryWrapper.in(Project::getId,projectIds).orderByDesc(Project::getCreateTime);

return this.list(queryWrapper);

    }



    @Override
    public void inviteMember(Long currentUserId, ProjectInviteDTO dto) {
  Project project = this.getById(dto.getProjectId());
        if (project == null) {
            throw new ServiceException("Project not found");
        }
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new ServiceException("You are not the owner of this project");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            throw new ServiceException("User not found");
        }
        LambdaQueryWrapper<ProjectMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.eq(ProjectMember::getProjectId, dto.getProjectId()).eq(ProjectMember::getUserId, user.getId());
        if (projectMemberMapper.selectCount(memberQueryWrapper) > 0) {
            throw new ServiceException("User is already a member or has been invited");
        }
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(dto.getProjectId());
        projectMember.setUserId(user.getId());
        projectMember.setRole("MEMBER");
        projectMember.setStatus(0);
        projectMemberMapper.insert(projectMember);
        Map<String, Object> message = new HashMap<>();
        message.put("type", "invite");
        message.put("projectId", dto.getProjectId());
        message.put("projectName", project.getName());
        message.put("UserId", user.getId());
        rabbitTemplate.convertAndSend(RabbitConfig.NOTIFY_QUEUE, message);
    }

    @Override
    public void handleInvite(Long currentUserId, InviteHandleDTO dto) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectMember::getProjectId, dto.getProjectId())
        .eq(ProjectMember::getUserId, currentUserId)
        .eq(ProjectMember::getStatus, 0);
        ProjectMember projectMember = projectMemberMapper.selectOne(queryWrapper);
        if (projectMember == null) {
            throw new ServiceException("Invite not found");
        }
        if(dto.getAccept()){
            projectMember.setStatus(1);
            projectMemberMapper.updateById(projectMember);
        }else{
            projectMemberMapper.deleteById(projectMember.getId());
        }
    }
}
