package com.ctecx.brs.tenant.transactions;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSupplierRequest {
    @NotBlank(message = "Supplier name cannot be blank")
    private String name;
    private String contactPerson;
}