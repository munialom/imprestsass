package com.ctecx.brs.tenant.imprestrepos;


import com.ctecx.brs.tenant.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Spring Data JPA automatically creates the implementation for this method
    List<Transaction> findByProjectId(Long projectId);
}