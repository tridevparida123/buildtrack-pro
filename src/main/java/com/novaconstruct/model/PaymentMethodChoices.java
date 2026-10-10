package com.novaconstruct.model;

public enum PaymentMethodChoices {
    BANK_TRANSFER("Bank Transfer (NEFT/RTGS)"),
    CHEQUE("Cheque"),
    UPI("UPI / Digital"),
    CASH("Site Cash Disbursement"),
    OTHER("Other");

    private final String displayName;

    PaymentMethodChoices(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
