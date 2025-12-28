package com.JobBNB.dev.job.dto;

import jakarta.validation.constraints.NotBlank;

public class SkillRequest {
    @NotBlank
    private String name;
}
