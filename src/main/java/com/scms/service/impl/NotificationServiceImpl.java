package com.scms.service.impl;

import com.scms.dto.NotificationDTO;
import com.scms.dto.PagedResponse;
import com.scms.entity.Notification;
import com.scms.exception.ResourceNotFoundException;
import com.scms.mapper.NotificationMapper;
import com.scms.repository.NotificationRepository;
import com.scms.security.SecurityUtils;
import com.scms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUserId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return notificationRepository.findByUserIdAndIsReadFalse(currentUserId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return notificationRepository.countByUserIdAndIsReadFalse(currentUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NotificationDTO> getNotificationsPaginated(int page, int size) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findByUserId(currentUserId, pageable);

        List<NotificationDTO> notificationDTOs = notificationPage.getContent().stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());

        return PagedResponse.<NotificationDTO>builder()
                .content(notificationDTOs)
                .page(notificationPage.getNumber())
                .size(notificationPage.getSize())
                .totalElements(notificationPage.getTotalElements())
                .totalPages(notificationPage.getTotalPages())
                .first(notificationPage.isFirst())
                .last(notificationPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(currentUserId);
        
        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });

        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        notificationRepository.delete(notification);
    }
}
