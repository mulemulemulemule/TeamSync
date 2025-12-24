package com.mule.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.model.dto.ProjectInviteDTO;

public interface ProjectService extends IService<Project> {

    void createProject(ProjectCreateDTO createDTO);

    public List<Project> listMyProjects(Long userId);

void inviteMember(Long currentUserId, ProjectInviteDTO dto);
}
