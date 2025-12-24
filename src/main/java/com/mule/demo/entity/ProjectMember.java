package com.mule.demo.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data
@TableName("project_member")
public class ProjectMember {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private Long userId;

    private String role;

    private Integer status;

    private String createTime;



}
