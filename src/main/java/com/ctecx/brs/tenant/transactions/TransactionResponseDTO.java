package com.ctecx.brs.tenant.transactions;


import com.ctecx.brs.tenant.enumutils.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private LocalDate postingDate; // User-provided date
    private LocalDateTime createdAt; // System timestamp
    private TransactionType type;
    private String narration;
    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal debtAmount;
    private String projectName;
    private String supplierName;
}