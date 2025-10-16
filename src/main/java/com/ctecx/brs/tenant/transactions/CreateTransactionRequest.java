package com.ctecx.brs.tenant.transactions;


import com.ctecx.brs.tenant.enumutils.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionRequest {
    @NotNull(message = "Posting date is required")
    @PastOrPresent(message = "Posting date cannot be in the future")
    private LocalDate postingDate;
    
    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @NotBlank(message = "Narration cannot be blank")
    private String narration;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private Long supplierId;
}