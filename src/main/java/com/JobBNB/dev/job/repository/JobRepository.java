package com.JobBNB.dev.job.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.JobBNB.dev.job.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByIsActiveTrue(Pageable pageable);

    Page<Job> findByLocationAndIsActiveTrue(
        String location, Pageable pageable
    );
}
