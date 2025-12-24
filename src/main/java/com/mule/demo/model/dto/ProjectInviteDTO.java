package com.mule.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInviteDTO {
@NotNull(message = "Project id cannot be null")
    private Long projectId;
@NotBlank(message = "Username cannot be empty")
    private String username;
}
