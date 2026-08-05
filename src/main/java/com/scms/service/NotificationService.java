package com.scms.service;

import com.scms.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotifications(Long userId);

    List<NotificationDTO> getUnreadNotifications(Long userId);

    Long getUnreadCount(Long userId);

    NotificationDTO getNotificationById(Long notificationId);

    NotificationDTO markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    void deleteNotification(Long notificationId);

}