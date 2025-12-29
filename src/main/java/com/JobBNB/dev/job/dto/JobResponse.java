package com.JobBNB.dev.job.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponse {

    private Long id;

    private String title;
    private String description;

    private String location;
    private String employmentType;

    private Integer minSalary;
    private Integer maxSalary;

    private Boolean isActive;

    private Instant createdAt;

    private Long createdById;
    
    private String createdByName;

    private String testUrl;
}
