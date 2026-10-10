package com.novaconstruct.model;

public enum ProjectStatusChoices {
    PLANNING("Planning & Permits"),
    ACTIVE("Active Construction"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed"),
    ARCHIVED("Archived");

    private final String displayName;

    ProjectStatusChoices(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
