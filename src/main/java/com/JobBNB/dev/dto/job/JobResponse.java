package com.JobBNB.dev.dto.job;

import java.time.Instant;

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
}
