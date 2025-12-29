package com.JobBNB.dev.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSearchRequest {

    private String keyword;
    private String location;
    private String employmentType;

    @jakarta.validation.constraints.Min(0)
    private Integer minSalary;
    @jakarta.validation.constraints.Min(0)
    private Integer maxSalary;

    private int page = 0;
    private int size = 10;

    @jakarta.validation.constraints.AssertTrue(message = "Search minimum salary cannot be greater than maximum salary")
    public boolean isSalaryRangeValid() {
        if (minSalary != null && maxSalary != null) {
            return minSalary <= maxSalary;
        }
        return true;
    }
}
