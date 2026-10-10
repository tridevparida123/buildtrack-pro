package com.novaconstruct.model;

public enum POStatusChoices {
    DRAFT("Draft"),
    ISSUED("Issued"),
    PARTIAL("Partially Delivered"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    POStatusChoices(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
