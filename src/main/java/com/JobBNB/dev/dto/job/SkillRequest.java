package com.JobBNB.dev.dto.job;

import jakarta.validation.constraints.NotBlank;

public class SkillRequest {
    @NotBlank
    private String name;
}
