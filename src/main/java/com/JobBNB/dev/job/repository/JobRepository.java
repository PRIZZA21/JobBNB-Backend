package com.JobBNB.dev.job.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.JobBNB.dev.job.model.Job;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Page<Job> findByIsActiveTrue(Pageable pageable);

    Page<Job> findByLocationAndIsActiveTrue(
            String location, Pageable pageable);

    List<Job> findByCreatedById(Long userId);

    Optional<Job> findById(Long jobId);
}
