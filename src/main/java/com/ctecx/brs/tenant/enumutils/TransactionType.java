package com.ctecx.brs.tenant.enumutils;

public enum TransactionType {
    // Non-cash event: Records a liability, does not affect cash balance
    SUPPLIER_DEBT,

    // Cash-In event: Increases cash balance
    BANK_WITHDRAWAL,

    // Cash-Out events: Decrease cash balance
    SUPPLIER_PAYMENT,
    ITEM_PURCHASE // For general expenses not linked to a specific supplier debt
}