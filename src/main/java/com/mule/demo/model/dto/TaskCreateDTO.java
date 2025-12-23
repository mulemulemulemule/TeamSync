package com.mule.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO {
@NotNull(message = "ProjectId cannot be empty")
    private Long projectId;

@NotBlank(message = "Task name cannot be empty")
    private String name;

    private String description;
    private Long assigneeId;
}
