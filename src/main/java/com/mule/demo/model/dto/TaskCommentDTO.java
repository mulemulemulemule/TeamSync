package com.mule.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCommentDTO {
    @NotNull
    private Long taskId;
    @NotBlank
    private String content;
}
