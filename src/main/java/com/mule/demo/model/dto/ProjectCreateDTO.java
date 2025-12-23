package com.mule.demo.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateDTO {
@NotBlank(message ="Project name cannot be empty")
@Size(min = 2, max = 20, message = "Project name must be between 2 and 20 characters")
private String name;

private String description;

private Long ownerId;
}

