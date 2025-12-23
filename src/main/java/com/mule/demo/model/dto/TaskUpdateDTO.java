package com.mule.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    @NotNull(message = "TaskId cannot be empty")
    private Long taskId;

    private Integer status;

    private Long assigneeId;

    private String description;


}
