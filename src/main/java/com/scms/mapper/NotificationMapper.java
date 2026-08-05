package com.scms.mapper;

import com.scms.dto.NotificationDTO;
import com.scms.entity.Notification;
import com.scms.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDTO toDTO(Notification notification) {

        if (notification == null) {
            return null;
        }

        return NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .referenceId(notification.getReferenceId())
                .createdAt(notification.getCreatedAt())
                .user(toUserSummary(notification.getUser()))
                .build();
    }

    private NotificationDTO.UserSummaryDTO toUserSummary(User user) {

        if (user == null) {
            return null;
        }

        return NotificationDTO.UserSummaryDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

}