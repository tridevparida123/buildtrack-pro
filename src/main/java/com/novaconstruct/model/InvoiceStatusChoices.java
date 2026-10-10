package com.novaconstruct.model;

public enum InvoiceStatusChoices {
    DRAFT("Draft"),
    PENDING_APPROVAL("Pending Approval"),
    UNPAID("Unpaid"),
    PARTIALLY_PAID("Partially Paid"),
    PAID("Paid"),
    OVERDUE("Overdue"),
    DISPUTED("Disputed"),
    CANCELLED("Cancelled");

    private final String displayName;

    InvoiceStatusChoices(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
