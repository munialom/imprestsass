package com.ctecx.brs.tenant.transactions;


import com.ctecx.brs.tenant.projects.Project;
import com.ctecx.brs.tenant.suppliers.Supplier;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the Project Imprest System API.
 * Provides all endpoints required by the React UI.
 */
@RestController
@RequestMapping("/api")
public class ImprestController {

    @Autowired
    private ImprestService imprestService;

    // --- Project Endpoints (for ProjectsList & AddNewProject) ---

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@Valid @RequestBody CreateProjectRequest request) {
        Project createdProject = imprestService.createProject(request);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
    
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = imprestService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // --- Supplier Endpoints (for SuppliersList & AddNewSupplier) ---

    @PostMapping("/suppliers")
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        Supplier createdSupplier = imprestService.createSupplier(request);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }
    
    /**
     * NOTE: Your provided API client did not have a GET for suppliers,
     * but it is required for the SuppliersList component. It is added here.
     */
    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        List<SupplierResponseDTO> suppliers = imprestService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // --- Transaction Endpoints (for DebtTransaction, CashTransaction, TransactionsList) ---

    @PostMapping("/transactions/debt")
    public ResponseEntity<Transaction> recordDebt(@Valid @RequestBody RecordDebtRequest request) {
        Transaction debtTransaction = imprestService.recordDebt(request);
        return new ResponseEntity<>(debtTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/transactions/cash")
    public ResponseEntity<Transaction> recordCashTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        Transaction cashTransaction = imprestService.recordCashTransaction(request);
        return new ResponseEntity<>(cashTransaction, HttpStatus.CREATED);
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> transactions = imprestService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // --- Financial Reporting Endpoint (for FinancialStatusReport) ---
    
    @GetMapping("/financial-status")
    public ResponseEntity<ProjectFinancialStatusDTO> getFinancialStatus(
            @RequestParam(required = false) Long projectId) {
        
        ProjectFinancialStatusDTO statusDTO = (projectId != null)
            ? imprestService.getFinancialStatusByProject(projectId)
            : imprestService.getOverallFinancialStatus();
        
        return ResponseEntity.ok(statusDTO);
    }
}