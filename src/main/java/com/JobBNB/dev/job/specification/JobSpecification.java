package com.JobBNB.dev.job.specification;

import com.JobBNB.dev.job.dto.JobSearchRequest;
import com.JobBNB.dev.job.model.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> filterJobs(JobSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter for active jobs
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));

            if (StringUtils.hasText(request.getKeyword())) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keyword);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        keyword);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            if (StringUtils.hasText(request.getLocation())) {
                predicates.add(criteriaBuilder.equal(root.get("location"), request.getLocation()));
            }

            if (StringUtils.hasText(request.getEmploymentType())) {
                predicates.add(criteriaBuilder.equal(root.get("employmentType"), request.getEmploymentType()));
            }

            if (request.getMinSalary() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), request.getMinSalary()));
            }

            if (request.getMaxSalary() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), request.getMaxSalary()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
