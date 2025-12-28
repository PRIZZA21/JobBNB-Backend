package com.JobBNB.dev.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JobSearchRequest {
    
    private String keyword;
    private String location;
    private String employmentType;

    private Integer minSalary;
    private Integer maxSalary;

    private int page = 0;
    private int size = 10;
}
