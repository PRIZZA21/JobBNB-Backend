package com.JobBNB.dev.job.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.JobBNB.dev.job.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByIsActiveTrue(Pageable pageable);

    Page<Job> findByLocationAndIsActiveTrue(
        String location, Pageable pageable
    );

    List<Job> findByCreatedById(Long userId);

}
