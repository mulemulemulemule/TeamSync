package com.mule.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
@Data
@TableName("project")//任务表
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private String createTime;

    private String updateTime;
    
    @TableLogic
    private Integer deleted;

}
