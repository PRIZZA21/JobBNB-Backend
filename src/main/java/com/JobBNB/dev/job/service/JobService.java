package com.JobBNB.dev.job.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.JobBNB.dev.job.model.Job;
import com.JobBNB.dev.user.repository.UserRepository;
import com.JobBNB.dev.common.exception.BusinessException;
import com.JobBNB.dev.job.dto.CreateJobRequest;
import com.JobBNB.dev.job.dto.JobResponse;
import com.JobBNB.dev.job.dto.JobSearchRequest;
import com.JobBNB.dev.job.dto.UpdateJobRequest;
import com.JobBNB.dev.job.mapper.JobMapper;
import com.JobBNB.dev.job.repository.JobRepository;
import com.JobBNB.dev.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public Page<JobResponse> listJobs(JobSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending());

        Page<Job> page;

        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            page = jobRepository.findByLocationAndIsActiveTrue(
                    request.getLocation(),
                    pageable);
        } else {
            page = jobRepository.findByIsActiveTrue(pageable);
        }

        return new PageImpl<>(
                page.getContent()
                        .stream()
                        .map(JobMapper::toResponse)
                        .peek(dto -> dto.setTestUrl(null))
                        .collect(Collectors.toList()),
                pageable,
                page.getTotalElements());
    }

    public JobResponse createJob(CreateJobRequest request) {
        Long employerId = (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new BusinessException("User not found"));

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setIsActive(true);
        job.setCreatedAt(Instant.now());
        job.setUpdatedAt(Instant.now());
        job.setCreatedBy(employer);
        job.setTestUrl(request.getTestUrl());

        Job saved = jobRepository.save(job);
        return JobMapper.toResponse(saved);
    }

    public JobResponse updateJob(Long jobId, UpdateJobRequest request) {
        Long userId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException("Job not found"));

        if (!job.getCreatedBy().getId().equals(userId)) {
            throw new BusinessException("Not allowed to update this job");
        }

        if (request.getTitle() != null)
            job.setTitle(request.getTitle());

        if (request.getDescription() != null)
            job.setDescription(request.getDescription());

        if (request.getLocation() != null)
            job.setLocation(request.getLocation());

        if (request.getEmploymentType() != null)
            job.setEmploymentType(request.getEmploymentType());

        if (request.getMinSalary() != null)
            job.setMinSalary(request.getMinSalary());

        if (request.getMaxSalary() != null)
            job.setMaxSalary(request.getMaxSalary());

        if (request.getIsActive() != null)
            job.setIsActive(request.getIsActive());
 
        if(request.getTestUrl() != null)
            job.setTestUrl(request.getTestUrl());

        job.setUpdatedAt(Instant.now());

        Job updated = jobRepository.save(job);
        return JobMapper.toResponse(updated);
    }

    public List<JobResponse> getMyJobs() {
        Long userId = (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return jobRepository
                .findByCreatedById(userId)
                .stream()
                .map(JobMapper::toResponse)
                .toList();
    }

    public JobResponse getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .map(JobMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Job not found"));
    }
}
