package com.JobBNB.dev.job.mapper;

import com.JobBNB.dev.job.model.Job;
import com.JobBNB.dev.job.dto.JobResponse;

public class JobMapper {
    private JobMapper() {}

    public static JobResponse toResponse(Job job) {
        JobResponse dto = new JobResponse();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setEmploymentType(job.getEmploymentType());
        dto.setMinSalary(job.getMinSalary());
        dto.setMaxSalary(job.getMaxSalary());
        dto.setCreatedById(job.getCreatedBy().getId());
        dto.setCreatedByName(job.getCreatedBy().getName());
        return dto;
    }
}
