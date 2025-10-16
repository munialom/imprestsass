package com.ctecx.brs.tenant.transactions;


import com.ctecx.brs.tenant.enumutils.TransactionType;
import com.ctecx.brs.tenant.imprestrepos.ProjectRepository;
import com.ctecx.brs.tenant.imprestrepos.SupplierRepository;
import com.ctecx.brs.tenant.imprestrepos.TransactionRepository;
import com.ctecx.brs.tenant.projects.Project;
import com.ctecx.brs.tenant.suppliers.Supplier;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImprestService {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private SupplierRepository supplierRepository;

    // --- Project and Supplier Management ---

    public Project createProject(CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        return projectRepository.save(project);
    }

    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(p -> new ProjectResponseDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    public Supplier createSupplier(CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        return supplierRepository.save(supplier);
    }
    
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(s -> new SupplierResponseDTO(s.getId(), s.getName(), s.getContactPerson()))
                .collect(Collectors.toList());
    }

    // --- Transaction Recording ---

    @Transactional
    public Transaction recordDebt(RecordDebtRequest request) {
        Project project = findProjectById(request.getProjectId());
        Supplier supplier = findSupplierById(request.getSupplierId());

        Transaction transaction = new Transaction();
        transaction.setPostingDate(request.getPostingDate()); // <-- SET THE USER'S DATE
        transaction.setProject(project);
        transaction.setSupplier(supplier);
        transaction.setType(TransactionType.SUPPLIER_DEBT);
        transaction.setNarration(request.getNarration());
        transaction.setDebtAmount(request.getAmount());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction recordCashTransaction(CreateTransactionRequest request) {
        Project project = findProjectById(request.getProjectId());
        List<Transaction> projectTransactions = transactionRepository.findByProjectId(request.getProjectId());

        Transaction transaction = new Transaction();
        transaction.setPostingDate(request.getPostingDate()); // <-- SET THE USER'S DATE
        transaction.setProject(project);
        transaction.setType(request.getTransactionType());
        transaction.setNarration(request.getNarration());

        switch (request.getTransactionType()) {
            case BANK_WITHDRAWAL:
                transaction.setCredit(request.getAmount());
                break;
            case ITEM_PURCHASE:
                validateCashBalance(projectTransactions, request.getAmount());
                transaction.setDebit(request.getAmount());
                break;
            case SUPPLIER_PAYMENT:
                if (request.getSupplierId() == null) {
                    throw new IllegalArgumentException("Supplier ID is required for a supplier payment.");
                }
                Supplier supplier = findSupplierById(request.getSupplierId());
                validateCashBalance(projectTransactions, request.getAmount());
                validateSupplierPayment(projectTransactions, request.getSupplierId(), request.getAmount());
                transaction.setDebit(request.getAmount());
                transaction.setSupplier(supplier);
                break;
            default:
                throw new IllegalArgumentException("Unsupported transaction type: " + request.getTransactionType());
        }
        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions and maps them to DTOs to prevent LazyInitializationException.
     * This method is transactional, so we can access related entities like Project and Supplier.
     *
     * @return A list of TransactionResponseDTO objects ready for serialization.
     */
    @Transactional(readOnly = true) // Use readOnly for performance on GET operations
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapTransactionToResponseDTO) // Convert each Transaction entity to a DTO
                .collect(Collectors.toList());
    }
    // --- Financial Reporting ---

    public ProjectFinancialStatusDTO getOverallFinancialStatus() {
        List<Transaction> allTransactions = transactionRepository.findAll();
        return calculateStatus(allTransactions.stream());
    }

    public ProjectFinancialStatusDTO getFinancialStatusByProject(Long projectId) {
        findProjectById(projectId);
        List<Transaction> projectTransactions = transactionRepository.findByProjectId(projectId);
        return calculateStatus(projectTransactions.stream());
    }

    // --- Private Helper and Validation Methods ---

    private ProjectFinancialStatusDTO calculateStatus(Stream<Transaction> transactionStream) {
        List<Transaction> transactions = transactionStream.toList();
        
        BigDecimal totalCredits = sum(transactions, TransactionType.BANK_WITHDRAWAL, Transaction::getCredit);
        BigDecimal totalSupplierPayments = sum(transactions, TransactionType.SUPPLIER_PAYMENT, Transaction::getDebit);
        BigDecimal totalItemPurchases = sum(transactions, TransactionType.ITEM_PURCHASE, Transaction::getDebit);
        BigDecimal totalDebits = totalSupplierPayments.add(totalItemPurchases);
        BigDecimal cashBalance = totalCredits.subtract(totalDebits);
        BigDecimal totalDebtIncurred = sum(transactions, TransactionType.SUPPLIER_DEBT, Transaction::getDebtAmount);
        BigDecimal outstandingDebt = totalDebtIncurred.subtract(totalSupplierPayments);
        BigDecimal netPosition = cashBalance.subtract(outstandingDebt);

        return new ProjectFinancialStatusDTO(cashBalance, totalDebtIncurred, totalSupplierPayments, outstandingDebt, netPosition);
    }

    private void validateCashBalance(List<Transaction> transactions, BigDecimal amountToSpend) {
        ProjectFinancialStatusDTO status = calculateStatus(transactions.stream());
        if (status.getImprestCashBalance().compareTo(amountToSpend) < 0) {
            throw new IllegalStateException("Insufficient cash balance for this project. Current balance: " + status.getImprestCashBalance() + ", attempted to spend: " + amountToSpend);
        }
    }

    private void validateSupplierPayment(List<Transaction> transactions, Long supplierId, BigDecimal paymentAmount) {
        BigDecimal totalDebt = transactions.stream()
            .filter(t -> t.getType() == TransactionType.SUPPLIER_DEBT && t.getSupplier() != null && Objects.equals(t.getSupplier().getId(), supplierId))
            .map(Transaction::getDebtAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = transactions.stream()
            .filter(t -> t.getType() == TransactionType.SUPPLIER_PAYMENT && t.getSupplier() != null && Objects.equals(t.getSupplier().getId(), supplierId))
            .map(Transaction::getDebit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outstandingBalance = totalDebt.subtract(totalPaid);
        if (paymentAmount.compareTo(outstandingBalance) > 0) {
            throw new IllegalStateException("Payment amount (" + paymentAmount + ") exceeds supplier's outstanding balance for this project (" + outstandingBalance + ").");
        }
    }



    private TransactionResponseDTO mapTransactionToResponseDTO(Transaction t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(t.getId());
        dto.setPostingDate(t.getPostingDate()); // <-- MAP THE POSTING DATE
        dto.setCreatedAt(t.getCreatedAt());   // <-- MAP THE CREATION TIMESTAMP
        dto.setType(t.getType());
        dto.setNarration(t.getNarration());
        dto.setCredit(t.getCredit());
        dto.setDebit(t.getDebit());
        dto.setDebtAmount(t.getDebtAmount());
        dto.setProjectName(t.getProject().getName());
        if (t.getSupplier() != null) {
            dto.setSupplierName(t.getSupplier().getName());
        }
        return dto;
    }


    private BigDecimal sum(List<Transaction> transactions, TransactionType type, Function<Transaction, BigDecimal> mapper) {
        return transactions.stream().filter(t -> t.getType() == type).map(mapper).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }
}