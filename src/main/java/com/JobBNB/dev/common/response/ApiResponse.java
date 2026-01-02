package com.JobBNB.dev.common.response;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final long timestamp;
    private final String requestId;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
        this.requestId = UUID.randomUUID().toString();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T details) {
        return new ApiResponse<>(false, message, details);
    }
}
