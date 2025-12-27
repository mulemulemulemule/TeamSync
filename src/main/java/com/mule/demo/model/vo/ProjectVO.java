package com.mule.demo.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Data;

@Data
public class ProjectVO {
private Long id;

    private String name;

    private String description;

    private Integer type;

    private Long likeCount;
    
    private Boolean isLiked;
}
