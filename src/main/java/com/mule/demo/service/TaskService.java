package com.mule.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.model.dto.TaskCreateDTO;
import com.mule.demo.entity.Task;
public interface TaskService extends IService<Task>{
void createTask(TaskCreateDTO createDTO);
}
