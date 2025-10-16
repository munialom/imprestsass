package com.ctecx.brs.tenant.transactions;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "Project name cannot be blank")
    private String name;
}