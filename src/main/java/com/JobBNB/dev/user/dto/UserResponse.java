package com.JobBNB.dev.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;

    // USER fields
    private String linkedinUrl;
    private String resumeUrl;

    // EMPLOYER fields
    private String companyUrl;
}
