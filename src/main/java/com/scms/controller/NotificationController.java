package com.scms.controller;

import com.scms.dto.NotificationDTO;
import com.scms.service.NotificationService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get User Notifications")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotifications(
            @PathVariable Long userId) {

        List<NotificationDTO> notifications = notificationService.getNotifications(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<NotificationDTO>>builder()
                        .success(true)
                        .message("Notifications Retrieved Successfully")
                        .data(notifications)
                        .build());
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get Unread Notifications")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getUnreadNotifications(
            @PathVariable Long userId) {

        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<NotificationDTO>>builder()
                        .success(true)
                        .message("Unread Notifications Retrieved Successfully")
                        .data(notifications)
                        .build());
    }

    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Unread Notification Count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @PathVariable Long userId) {

        Long count = notificationService.getUnreadCount(userId);

        return ResponseEntity.ok(
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("Unread Count Retrieved Successfully")
                        .data(count)
                        .build());
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark Notification As Read")
    public ResponseEntity<ApiResponse<NotificationDTO>> markAsRead(
            @PathVariable Long notificationId) {

        NotificationDTO notification = notificationService.markAsRead(notificationId);

        return ResponseEntity.ok(
                ApiResponse.<NotificationDTO>builder()
                        .success(true)
                        .message("Notification Marked As Read")
                        .data(notification)
                        .build());
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "Mark All Notifications As Read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @PathVariable Long userId) {

        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("All Notifications Marked As Read")
                        .build());
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete Notification")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long notificationId) {

        notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Notification Deleted Successfully")
                        .build());
    }

}