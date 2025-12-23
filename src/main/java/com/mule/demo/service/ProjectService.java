package com.mule.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.ProjectCreateDTO;

public interface ProjectService extends IService<Project> {

    void createProject(ProjectCreateDTO createDTO);

}
