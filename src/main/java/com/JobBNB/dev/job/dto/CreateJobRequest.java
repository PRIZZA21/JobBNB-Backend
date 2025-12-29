package com.JobBNB.dev.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String location;

    @NotBlank
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT

    @NotNull
    private Integer minSalary;

    @NotNull
    private Integer maxSalary;

    @NotNull
    private String testUrl;
}
