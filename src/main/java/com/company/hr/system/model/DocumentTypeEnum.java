package com.company.hr.system.model;

public enum DocumentTypeEnum {
    NATIONAL_ID("National ID"),
    CONTRACT("Contract"),
    CERTIFICATION("Certification");

    private final String displayName;

    DocumentTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}