package com.mule.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
@Data
@TableName("task")
public class Task {
@TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String name;

    private String description;

    private Integer status;

    private Long assigneeId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
