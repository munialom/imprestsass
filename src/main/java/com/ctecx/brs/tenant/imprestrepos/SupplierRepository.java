package com.ctecx.brs.tenant.imprestrepos;


import com.ctecx.brs.tenant.suppliers.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {}