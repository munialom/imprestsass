package com.ctecx.brs.tenant.exception;

public abstract class TransactionException extends RuntimeException {
    private final String errorCode;

    public TransactionException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Subclass for invalid input (e.g., missing entities)
    public static class InvalidInputException extends TransactionException {
        public InvalidInputException(String message, String errorCode) {
            super(message, errorCode);
        }
    }

    // Subclass for invalid payment amounts
    public static class InvalidAmountException extends TransactionException {
        public InvalidAmountException(String message, String errorCode) {
            super(message, errorCode);
        }
    }

    // Subclass for invalid stock quantities
    public static class InvalidStockException extends TransactionException {
        public InvalidStockException(String message, String errorCode) {
            super(message, errorCode);
        }
    }
}