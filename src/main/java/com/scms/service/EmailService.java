package com.scms.service;

import com.scms.dto.ComplaintResponse;
import com.scms.dto.NotificationDTO;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String resetToken);

    void sendWelcomeEmail(String toEmail, String firstName);

    void sendComplaintCreatedEmail(String toEmail,
            ComplaintResponse complaint);

    void sendComplaintAssignedEmail(String toEmail,
            ComplaintResponse complaint);

    void sendComplaintStatusUpdateEmail(String toEmail,
            ComplaintResponse complaint);

    void sendComplaintResolvedEmail(String toEmail,
            ComplaintResponse complaint);

    void sendNotificationEmail(String toEmail,
            NotificationDTO notification);

}