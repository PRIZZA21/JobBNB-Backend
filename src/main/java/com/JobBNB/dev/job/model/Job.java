package com.JobBNB.dev.job.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.JobBNB.dev.user.model.User;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; 
}
