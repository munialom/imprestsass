package com.ctecx.brs.tenant.utils;

public class PurchaseTransactionException extends RuntimeException {
    public static final PurchaseTransactionException BRANCH_NOT_FOUND = new PurchaseTransactionException("Branch not found");
    public static final PurchaseTransactionException VENDOR_NOT_FOUND = new PurchaseTransactionException("Vendor not found");
    public static final PurchaseTransactionException PRODUCT_NOT_FOUND = new PurchaseTransactionException("Product not found");
    public static final PurchaseTransactionException TRANSACTION_NOT_FOUND = new PurchaseTransactionException("Transaction not found");
    public static final PurchaseTransactionException PURCHASE_FAILED = new PurchaseTransactionException("Failed to create purchase transactions");
    public static final PurchaseTransactionException ACCOUNTING_ENTRY_FAILED = new PurchaseTransactionException("Failed to create accounting entries");
    public static final PurchaseTransactionException BATCH_NOT_FOUND = new PurchaseTransactionException("Product batch not found");

    public PurchaseTransactionException(String message) {
        super(message);
    }

    public PurchaseTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}