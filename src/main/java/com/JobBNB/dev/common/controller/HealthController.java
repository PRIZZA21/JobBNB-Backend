package com.JobBNB.dev.common.controller;

import com.JobBNB.dev.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.success("JobBNB Backend is Live!", Map.of(
                "status", "UP",
                "version", "0.0.1-SNAPSHOT"));
    }
}
