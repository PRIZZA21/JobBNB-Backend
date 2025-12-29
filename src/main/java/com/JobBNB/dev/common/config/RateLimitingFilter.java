package com.JobBNB.dev.common.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // General permit: 20 requests per minute
    private final Bandwidth generalLimit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));

    // Auth permit: 5 requests per minute (Stricter for login/register)
    private final Bandwidth authLimit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip rate limiting for non-API calls if any
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        String cacheKey = ip + ":" + (path.startsWith("/api/auth") ? "auth" : "gen");

        Bucket bucket = buckets.computeIfAbsent(cacheKey, k -> Bucket.builder()
                .addLimit(path.startsWith("/api/auth") ? authLimit : generalLimit)
                .build());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Too many requests. Please try again later.\"}");
        }
    }
}
