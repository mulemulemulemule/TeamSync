package com.mule.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.InviteHandleDTO;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;
import com.mule.demo.model.dto.ProjectTypeDTO;
import com.mule.demo.model.vo.ProjectMemberVO;
import com.mule.demo.model.vo.ProjectVO;

public interface ProjectService extends IService<Project> {

    void createProject(ProjectCreateDTO createDTO);

    public List<ProjectVO> listMyProjects(Long userId);

void inviteMember(Long currentUserId, ProjectInviteDTO dto);

void handleInvite(Long currentUserId, InviteHandleDTO dto);

public List<Project> listPendingInvites(Long userId);

public List<ProjectVO> listPublicProjects(String keyword);
public List<ProjectMemberVO> listMembers(Long projectId);
public void toggleLike(Long projectId);

void deleteProject(Long projectId);

void updateProjectType(ProjectTypeDTO dto);
}
