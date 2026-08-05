package com.scms.dto;

import com.scms.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;

    private String title;

    private String message;

    private NotificationType type;

    private Boolean isRead;

    private LocalDateTime readAt;

    private Long referenceId;

    private LocalDateTime createdAt;

    private UserSummaryDTO user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummaryDTO {

        private Long id;

        private String firstName;

        private String lastName;

        private String email;

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
}