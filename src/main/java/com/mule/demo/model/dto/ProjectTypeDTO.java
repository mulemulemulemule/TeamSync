package com.mule.demo.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class ProjectTypeDTO {
@NotNull
private long id;
@NotNull
private Integer type;
}
