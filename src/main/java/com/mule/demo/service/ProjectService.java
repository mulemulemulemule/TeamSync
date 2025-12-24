package com.mule.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.InviteHandleDTO;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.vo.ProjectMemberVO;

public interface ProjectService extends IService<Project> {

    void createProject(ProjectCreateDTO createDTO);

    public List<Project> listMyProjects(Long userId);

void inviteMember(Long currentUserId, ProjectInviteDTO dto);

void handleInvite(Long currentUserId, InviteHandleDTO dto);

public List<Project> listPendingInvites(Long userId);

public List<Project> listPublicProjects();
public List<ProjectMemberVO> listMembers(Long projectId);
}
