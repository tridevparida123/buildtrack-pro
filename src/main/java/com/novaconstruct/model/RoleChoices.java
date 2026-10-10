package com.novaconstruct.model;

public enum RoleChoices {
    SUPER_ADMIN("Super Administrator"),
    COMPANY_ADMIN("Company Administrator"),
    PROJECT_MANAGER("Project Manager"),
    ACCOUNTANT("Accountant"),
    LEGAL_OFFICER("Legal Officer"),
    SITE_SUPERVISOR("Site Supervisor"),
    AUDITOR("Read-only Auditor");

    private final String displayName;

    RoleChoices(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
