package com.JobBNB.dev.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = frontendUrl + "/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your JobBNB Account");
        message.setText("Thank you for registering! Please click the link below to verify your email address:\n\n"
                + verificationUrl);

        mailSender.send(message);
        log.info("Verification email sent to {}", to);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset your JobBNB Password");
        message.setText(
                "You requested to reset your password. Click the link below to set a new password. This link will expire in 1 hour:\n\n"
                        + resetUrl);

        mailSender.send(message);
        log.info("Password reset email sent to {}", to);
    }
}
