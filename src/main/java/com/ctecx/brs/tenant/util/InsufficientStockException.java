package com.ctecx.brs.tenant.util;



public class InsufficientStockException extends RuntimeException {

    private String itemId;
    private int requestedQuantity;
    private int availableQuantity;

    public InsufficientStockException(String itemId, int requestedQuantity, int availableQuantity) {
        super(String.format("Insufficient stock for item %s. Requested: %d, Available: %d",
                itemId, requestedQuantity, availableQuantity));
        this.itemId = itemId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}