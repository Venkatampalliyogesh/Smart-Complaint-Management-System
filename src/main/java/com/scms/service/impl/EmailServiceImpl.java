package com.scms.service.impl;

import com.scms.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${app.frontend.url:http://localhost:8080}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = frontendUrl + "/reset-password.html?token=" + resetToken;
        String subject = "SCMS Password Reset Request";
        String body = "You requested a password reset for your Smart Complaint Management System account.\n\n"
                + "Click the link below to reset your password (valid for 1 hour):\n"
                + resetLink + "\n\n"
                + "If you did not request this, please ignore this email.";

        if (!StringUtils.hasText(mailUsername)) {
            log.warn("Mail not configured. Password reset link for {}: {}", toEmail, resetLink);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("Password reset email sent to {}", toEmail);
    }
}
