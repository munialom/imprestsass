package com.ctecx.brs.tenant.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierResponseDTO {
    private Long id;
    private String name;
    private String contactPerson;
}