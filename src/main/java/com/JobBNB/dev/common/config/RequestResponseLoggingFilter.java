package com.JobBNB.dev.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().contains("/api/")) {
            long startTime = System.currentTimeMillis();

            // We wrap the request/response to allow reading the body multiple times if
            // needed
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            try {
                filterChain.doFilter(requestWrapper, responseWrapper);
            } finally {
                long duration = System.currentTimeMillis() - startTime;

                log.info("API Request: [{} {}] | Status: {} | Duration: {}ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);

                // Important: copy body back to response else the client gets an empty body
                responseWrapper.copyBodyToResponse();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
