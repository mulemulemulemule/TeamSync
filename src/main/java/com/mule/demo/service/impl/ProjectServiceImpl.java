package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.entity.Project;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.ProjectMapper;
import com.mule.demo.model.dto.ProjectCreateDTO;
import com.mule.demo.service.ProjectService;
import org.springframework.stereotype.Service;


@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService{
@Override
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
        this.save(project);
    }
}
