package com.JobBNB.dev.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jobs")
@Getter @Setter
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employerId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private String employmentType;

    private Integer minSalary;
    private Integer maxSalary;

    private Boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;
}
