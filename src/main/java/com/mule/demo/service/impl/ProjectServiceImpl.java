package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.common.UserContext;
import com.mule.demo.entity.Project;
import com.mule.demo.entity.ProjectMember;
import com.mule.demo.entity.User;
import com.mule.demo.entity.Task;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.ProjectMapper;
import com.mule.demo.mapper.ProjectMemberMapper;
import com.mule.demo.mapper.TaskMapper;
import com.mule.demo.mapper.UserMapper;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.dto.ProjectTypeDTO;
import com.mule.demo.model.dto.InviteHandleDTO;
import com.mule.demo.model.vo.ProjectMemberVO;
import com.mule.demo.model.vo.ProjectVO;
import com.mule.demo.service.RabbitMQService;
import com.mule.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mule.demo.service.RedisService;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
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
    @Autowired
    private RedisService redisService;
    @Autowired
    private TaskMapper taskMapper;

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
    public List<ProjectVO> listMyProjects(Long userId) {
        List<Long> ids = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 1);
    if (ids.isEmpty()) return new ArrayList<>();
    LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
    wrapper.in(Project::getId, ids).orderByDesc(Project::getCreateTime);
    List<Project> projects = this.list(wrapper);
    return convertToVO(projects);
    
    }

    @Override
    public List<Project> listPendingInvites(Long userId) {
        List<Long> ids = projectMemberMapper.selectProjectIdsByUserIdAndStatus(userId, 0);
        return ids.isEmpty() ? Collections.emptyList() : this.list(new LambdaQueryWrapper<Project>().in(Project::getId, ids).orderByDesc(Project::getCreateTime));
    }

    //私有方法转换vo
    private List<ProjectVO> convertToVO(List<Project> projects){
        List<ProjectVO> voList = new ArrayList<>();
         Long currentUserId = UserContext.getUserId();
         for (Project p : projects) {
ProjectVO vo = new ProjectVO();
BeanUtils.copyProperties(p, vo);
Double score = redisService.getZScore("project:rank", p.getId());
vo.setLikeCount(score == null ? 0 : score.longValue());
boolean isLiked = redisService.sHasKey("project:like:detail:" + p.getId(), currentUserId);
vo.setIsLiked(isLiked);
voList.add(vo);
         }
         return voList;
    }

    @Override
    public List<ProjectVO> listPublicProjects(String keyword) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getType, "1").orderByDesc(Project::getCreateTime);
        if(StrUtil.isNotBlank(keyword)) wrapper.like(Project::getName, keyword);
        List<Project> projects =this.list(wrapper);
        return convertToVO(projects);
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

    @Override
    public void toggleLike(Long projectId){
Long userId= UserContext.getUserId();
String detailkey = "project:like:detail:" + projectId;
String rankKey = "project:rank";
if(redisService.sHasKey(detailkey, userId)){
    redisService.sRemove(detailkey, userId);
    redisService.zIncr(rankKey, projectId, -1);
}
else{
    redisService.sSet(detailkey, userId);
    redisService.zIncr(rankKey, projectId, 1);
}
}

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Long currentUserId = UserContext.getUserId();
        Project project = this.getById(projectId);
        if(project==null) throw new ServiceException("Project not found");
        if(!project.getOwnerId().equals(currentUserId)) throw new ServiceException(403, "Forbidden");

        LambdaQueryWrapper<Task> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.eq(Task::getProjectId, projectId);
        taskMapper.delete(taskWrapper);

        LambdaQueryWrapper<ProjectMember> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(ProjectMember::getProjectId, projectId);
        projectMemberMapper.delete(memberWrapper);

        redisService.delete("project:like:detail:" + projectId);
        redisService.zRemove("project:rank", projectId);

        this.removeById(projectId);
    }

    @Override
    public void updateProjectType(ProjectTypeDTO dto) {
        Project project = this.getById(dto.getId());
        if(project==null) throw new ServiceException("Project not found");
        if (!project.getOwnerId().equals(UserContext.getUserId())) throw new ServiceException(403, "only owner can change project type");
        project.setType(dto.getType());
        this.updateById(project);
        
    }
    }

