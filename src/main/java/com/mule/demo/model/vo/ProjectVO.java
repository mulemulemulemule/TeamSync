package com.mule.demo.model.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectVO {
private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private Integer type;

    private LocalDateTime createTime;

    private Long likeCount;
    
    private Boolean isLiked;
}
