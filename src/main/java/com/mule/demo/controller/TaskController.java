package com.mule.demo.controller;

import com.mule.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mule.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.mule.demo.model.dto.TaskCommentDTO;
import com.mule.demo.model.dto.TaskCreateDTO;
import java.util.List;
import java.util.Map;
import com.mule.demo.entity.Task;
import com.mule.demo.model.dto.TaskUpdateDTO;
import com.mule.demo.model.vo.TaskCommentVO;

@Tag(name="任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary ="创建任务")
    @PostMapping("/create")
    public Result<String> create(@RequestBody @Valid TaskCreateDTO dto) {
taskService.createTask(dto);
        return Result.success("任务创建成功");
    }

    @Operation(summary ="获取任务看板")
    @GetMapping("/board/{projectId}")
    public Result<Map<Integer,List<Task>>> getTaskBoard(@PathVariable Long projectId) {
        return Result.success(taskService.getTaskBoard(projectId));
    }
    @Operation(summary ="更新任务")
    @PostMapping("/update")
    public Result<String> updateTask(@RequestBody @Valid TaskUpdateDTO dto) {
        taskService.updateTask(dto);
        return Result.success("update success");
    }
    @Operation(summary ="上传任务文件")
    @PostMapping("/upload/file/{taskId}")
    public Result<String> uploadTaskFile(@PathVariable Long taskId, @RequestParam MultipartFile file) {
         if (file.isEmpty()) return Result.error("file is empty");
        return Result.success(taskService.uploadTaskFile(taskId, file));
    }
    @Operation(summary ="发表评论")
    @PostMapping("/comment/add")
    public Result<String> addComment(@RequestBody @Valid TaskCommentDTO dto) {
        taskService.addComment(dto);
        return Result.success("comment success");
    }
    @Operation(summary ="获取评论列表") 
    @GetMapping("/comment/list/{taskId}")
    public Result<List<TaskCommentVO>> listComments(@PathVariable Long taskId) {
        return Result.success(taskService.listComments(taskId));
    }
    @Operation(summary ="删除评论")
    @DeleteMapping("/comment/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        taskService.deleteComment(id);
        return Result.success("delete success");
    }
}
