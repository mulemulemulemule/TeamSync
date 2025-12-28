package com.mule.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.mule.demo.model.dto.TaskUpdateDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.model.dto.TaskCreateDTO;
import com.mule.demo.entity.Task;
public interface TaskService extends IService<Task>{
void createTask(TaskCreateDTO createDTO);

Map<Integer, List<Task>> getTaskBoard(Long projectId);
void updateTask(TaskUpdateDTO updateDTO);

public String uploadTaskFile(Long taskId, MultipartFile file);
}
