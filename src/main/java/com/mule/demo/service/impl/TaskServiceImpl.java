package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.stream.Collectors;
import com.mule.demo.entity.Task;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.ProjectMapper;
import com.mule.demo.mapper.TaskMapper;
import java.util.List;
import com.mule.demo.model.dto.TaskCreateDTO;
import com.mule.demo.service.TaskService;
import com.mule.demo.websocket.WebSocketServer;
import com.mule.demo.entity.Project;
import com.mule.demo.model.dto.TaskUpdateDTO;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service

public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService{
    @Autowired
     private ProjectMapper projectMapper;
@Override
    public void createTask(TaskCreateDTO dto) {
        Project project = projectMapper.selectById(dto.getProjectId());
        if (project == null) {
            throw new ServiceException("Project not found");
        }

LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getName,dto.getName()).eq(Task::getProjectId, dto.getProjectId());
       if (this.count(queryWrapper) > 0) {
           throw new ServiceException("Task name already exists");
       }

        Task task = new Task();

        task.setProjectId(dto.getProjectId());

        task.setName(dto.getName());

        task.setDescription(dto.getDescription());

        task.setAssigneeId(dto.getAssigneeId());

        task.setStatus(0);

        this.save(task);
        WebSocketServer.sendInfo(dto.getProjectId(), "new task,refresh");
    }
    @Override
    public Map<Integer,List<Task>> getTaskBoard(Long projectId) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getProjectId, projectId).orderByDesc(Task::getCreateTime);
        List<Task> tasks = this.list(queryWrapper);
        return tasks.stream().collect(Collectors.groupingBy(Task::getStatus));
    }
    @Override
    public void updateTask(TaskUpdateDTO dto) {
        Task task = this.getById(dto.getTaskId());
        if (task == null) {
            throw new ServiceException("Task not found");
        }
        Task updateTask = new Task();
        updateTask.setId(dto.getTaskId());

        if (dto.getStatus() != null) {
            updateTask.setStatus(dto.getStatus());
        }
        if (dto.getDescription() != null) {
            updateTask.setDescription(dto.getDescription());
        }
        if (dto.getAssigneeId() != null) {
            updateTask.setAssigneeId(dto.getAssigneeId());
        }
        this.updateById(updateTask);
        WebSocketServer.sendInfo(task.getProjectId(), "update task,refresh");
    }
    }

