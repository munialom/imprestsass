package com.ctecx.brs.tenant.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFinancialStatusDTO {
    private BigDecimal imprestCashBalance;
    private BigDecimal totalDebtIncurred;
    private BigDecimal totalDebtPaid;
    private BigDecimal outstandingDebtBalance;
    private BigDecimal netFinancialPosition;
}