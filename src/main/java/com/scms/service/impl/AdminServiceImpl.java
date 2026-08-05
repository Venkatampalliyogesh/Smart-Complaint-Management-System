package com.scms.service.impl;

import com.scms.dto.DashboardDTO;
import com.scms.dto.PagedResponse;
import com.scms.dto.UserDTO;
import com.scms.entity.Complaint;
import com.scms.entity.Role;
import com.scms.entity.User;
import com.scms.enums.ComplaintStatus;
import com.scms.enums.UserRole;
import com.scms.exception.BadRequestException;
import com.scms.exception.ResourceNotFoundException;
import com.scms.mapper.UserMapper;
import com.scms.repository.ComplaintRepository;
import com.scms.repository.RoleRepository;
import com.scms.repository.UserRepository;
import com.scms.security.SecurityUtils;
import com.scms.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO getDashboardStatistics() {
        User currentUser = getCurrentUser();

        DashboardDTO.DashboardDTOBuilder builder = DashboardDTO.builder();

        // User statistics
        builder.totalUsers(userRepository.count());
        builder.activeUsers(userRepository.countByActiveTrue());

        // Complaint statistics
        builder.totalComplaints(complaintRepository.count());
        builder.openComplaints(complaintRepository.countByStatus(ComplaintStatus.SUBMITTED));
        builder.inProgressComplaints(complaintRepository.countByStatus(ComplaintStatus.IN_PROGRESS));
        builder.resolvedComplaints(complaintRepository.countByStatus(ComplaintStatus.RESOLVED));
        builder.closedComplaints(complaintRepository.countByStatus(ComplaintStatus.CLOSED));
        builder.rejectedComplaints(complaintRepository.countByStatus(ComplaintStatus.REJECTED));

        // Assigned to current user
        builder.assignedToMe(complaintRepository.countByAssignedToId(currentUser.getId()));

        // Complaints by status
        Map<String, Long> complaintsByStatus = new HashMap<>();
        for (ComplaintStatus status : ComplaintStatus.values()) {
            complaintsByStatus.put(status.name(), complaintRepository.countByStatus(status));
        }
        builder.complaintsByStatus(complaintsByStatus);

        // Complaints by priority (simplified - would need priority repository)
        Map<String, Long> complaintsByPriority = new HashMap<>();
        builder.complaintsByPriority(complaintsByPriority);

        // Complaints by category (simplified - would need category repository)
        Map<String, Long> complaintsByCategory = new HashMap<>();
        builder.complaintsByCategory(complaintsByCategory);

        // Users by role - optimized with repository query
        Map<String, Long> usersByRole = new HashMap<>();
        for (UserRole role : UserRole.values()) {
            usersByRole.put(role.name(), userRepository.countByRole(role));
        }
        builder.usersByRole(usersByRole);

        // Staff and admin counts
        builder.totalStaff(usersByRole.getOrDefault(UserRole.ROLE_STAFF.name(), 0L));
        builder.totalAdmins(usersByRole.getOrDefault(UserRole.ROLE_ADMIN.name(), 0L));

        return builder.build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return PagedResponse.<UserDTO>builder()
                .content(userDTOs)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public UserDTO updateUserStatus(Long userId, Boolean active) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setActive(active);
        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public UserDTO updateUserRole(Long userId, String roleName) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        try {
            UserRole newRole = UserRole.valueOf("ROLE_" + roleName.toUpperCase());

            Role role = roleRepository.findByName(newRole)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + newRole));

            // Clear existing roles and set new role
            user.getRoles().clear();
            user.getRoles().add(role);

            User saved = userRepository.save(user);
            return userMapper.toDTO(saved);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role name: " + roleName);
        }
    }

    private User getCurrentUser() {
        return userRepository.findByEmailWithRoles(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
