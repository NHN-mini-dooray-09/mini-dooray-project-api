package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

public enum ProjectAuthorityType {
    PROJECT_ROLE_ADMIN("관리자"),
    PROJECT_ROLE_MEMBER("멤버");

    private final String label;

    ProjectAuthorityType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
