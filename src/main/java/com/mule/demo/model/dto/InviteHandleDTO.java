package com.mule.demo.model.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteHandleDTO {
    @NotNull(message = "projectId cannot be empty")
    private Long projectId;
    @NotNull(message = "accept cannot be empty")
    private Boolean accept;

}
