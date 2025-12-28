package com.JobBNB.dev.job.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.JobBNB.dev.job.dto.JobResponse;
import com.JobBNB.dev.job.dto.JobSearchRequest;
import com.JobBNB.dev.job.dto.CreateJobRequest;
import com.JobBNB.dev.job.service.JobService;
import jakarta.validation.Valid;
import com.JobBNB.dev.job.dto.UpdateJobRequest;
import lombok.extern.slf4j.Slf4j;
import com.JobBNB.dev.common.response.ApiResponse;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ApiResponse<Page<JobResponse>> listJobs(JobSearchRequest request) {
        log.info("Received request to list jobs with filters: {}", request);
        Page<JobResponse> result = jobService.listJobs(request);
        return ApiResponse.success(result);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ApiResponse<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        log.info("Received request to create a new job: {}", request.getTitle());
        JobResponse response = jobService.createJob(request);
        return ApiResponse.success("Job created successfully", response);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{id}")
    public ApiResponse<JobResponse> updateJob(@PathVariable Long id, @RequestBody UpdateJobRequest request) {
        log.info("Received request to update job ID: {}", id);
        JobResponse response = jobService.updateJob(id, request);
        return ApiResponse.success("Job updated successfully", response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ApiResponse<List<JobResponse>> myJobs() {
        log.info("Received request to fetch jobs for the current employer");
        List<JobResponse> response = jobService.getMyJobs();
        return ApiResponse.success(response);
    }

}
