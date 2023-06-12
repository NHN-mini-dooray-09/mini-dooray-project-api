package com.nhnacademy.minidoorayprojectapi.domain.project.entity;

public enum ProjectStatusType {
    PROGRESS("진행"),
    ARCHIVE("보관");

    private final String label;

    ProjectStatusType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
