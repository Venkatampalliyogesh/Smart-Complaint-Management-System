package com.scms.service.impl;

import com.scms.dto.DashboardDTO;
import com.scms.enums.ComplaintStatus;
import com.scms.repository.ComplaintRepository;
import com.scms.repository.NotificationRepository;
import com.scms.repository.RoleRepository;
import com.scms.repository.CategoryRepository;
import com.scms.repository.UserRepository;
import com.scms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationRepository notificationRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getUserDashboard(Long userId) {

        return DashboardDTO.builder()
                .totalComplaints(
                        complaintRepository.countByUserId(userId))
                .submittedComplaints(
                        complaintRepository.countByUserIdAndStatus(
                                userId,
                                ComplaintStatus.SUBMITTED))
                .assignedComplaints(
                        complaintRepository.countByUserIdAndStatus(
                                userId,
                                ComplaintStatus.ASSIGNED))
                .inProgressComplaints(
                        complaintRepository.countByUserIdAndStatus(
                                userId,
                                ComplaintStatus.IN_PROGRESS))
                .resolvedComplaints(
                        complaintRepository.countByUserIdAndStatus(
                                userId,
                                ComplaintStatus.RESOLVED))
                .closedComplaints(
                        complaintRepository.countByUserIdAndStatus(
                                userId,
                                ComplaintStatus.CLOSED))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getAdminDashboard() {

        return DashboardDTO.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByActiveTrue())
                .totalCategories((long) categoryRepository.findByActiveTrue().size())
                .totalNotifications(notificationRepository.count())
                .build();
    }
}