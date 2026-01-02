package com.JobBNB.dev.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    @NotBlank
    private String idToken;

    @NotBlank
    private String role; // So we know if the new user is a CANDIDATE or EMPLOYER
}
