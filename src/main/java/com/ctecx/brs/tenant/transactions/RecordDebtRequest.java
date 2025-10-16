package com.ctecx.brs.tenant.transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent; // For validation
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordDebtRequest {
    @NotNull(message = "Posting date is required")
    @PastOrPresent(message = "Posting date cannot be in the future")
    private LocalDate postingDate;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Narration/Invoice details cannot be blank")
    private String narration;

    @NotNull(message = "Debt amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
}