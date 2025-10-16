package com.ctecx.brs.tenant.util;


public enum DocumentType {
    PURCHASE_VOUCHER("PV"),
    SALES_VOUCHER("SV"),
    JOURNAL_VOUCHER("JV"),
    PAYMENT_VOUCHER("PMT"),
    RECEIPT_VOUCHER("RCT"),
    CONTRA_VOUCHER("CV"),
    CREDIT_NOTE("CN"),
    DEBIT_NOTE("DN");

    private final String prefix;

    DocumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static DocumentType fromPrefix(String prefix) {
        for (DocumentType type : values()) {
            if (type.getPrefix().equals(prefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown prefix: " + prefix);
    }
}