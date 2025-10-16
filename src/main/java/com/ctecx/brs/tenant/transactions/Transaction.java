package com.ctecx.brs.tenant.transactions;


import com.ctecx.brs.tenant.enumutils.TransactionType;
import com.ctecx.brs.tenant.projects.Project;
import com.ctecx.brs.tenant.suppliers.Supplier;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp; // Import this

import java.math.BigDecimal;
import java.time.LocalDate; // Use LocalDate for user input
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user-provided date for the transaction (for financial reporting)
    @Column(nullable = false)
    private LocalDate postingDate; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String narration;

    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal debit = BigDecimal.ZERO;
    private BigDecimal debtAmount = BigDecimal.ZERO;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // The system-generated timestamp for when the record was created (for auditing)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}