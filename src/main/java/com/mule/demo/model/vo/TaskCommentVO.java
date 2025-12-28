package com.mule.demo.model.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskCommentVO {
private long id;
private String content;
private LocalDateTime createTime;

private Long userId;
private String username;
private String Avatar;
}
