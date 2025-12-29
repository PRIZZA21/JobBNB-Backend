package com.JobBNB.dev.job.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateJobRequest {

    @Size(max = 150)
    private String title;

    private String description;

    private String location;

    private String employmentType;

    private Integer minSalary;
    private Integer maxSalary;

    private Boolean isActive;

    private String testUrl;
}
