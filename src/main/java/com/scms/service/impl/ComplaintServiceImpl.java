package com.scms.service.impl;

import com.scms.dto.CategoryDTO;
import com.scms.dto.ComplaintAssignRequest;
import com.scms.dto.ComplaintHistoryDTO;
import com.scms.dto.ComplaintRequest;
import com.scms.dto.ComplaintResponse;
import com.scms.dto.ComplaintStatusUpdateRequest;
import com.scms.dto.ComplaintUpdateDTO;
import com.scms.dto.PagedResponse;
import com.scms.dto.UserSummaryDTO;
import com.scms.entity.Category;
import com.scms.entity.Complaint;
import com.scms.entity.ComplaintHistory;
import com.scms.entity.Priority;
import com.scms.entity.User;
import com.scms.enums.ComplaintStatus;
import com.scms.enums.UserRole;
import com.scms.exception.BadRequestException;
import com.scms.exception.ResourceNotFoundException;
import com.scms.exception.UnauthorizedException;
import com.scms.mapper.ComplaintMapper;
import com.scms.repository.CategoryRepository;
import com.scms.repository.ComplaintHistoryRepository;
import com.scms.repository.ComplaintRepository;
import com.scms.repository.ComplaintSpecification;
import com.scms.repository.PriorityRepository;
import com.scms.repository.UserRepository;
import com.scms.security.CustomUserDetails;
import com.scms.security.SecurityUtils;
import com.scms.service.ComplaintService;
import com.scms.validation.ComplaintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintHistoryRepository complaintHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final PriorityRepository priorityRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;
    private final ComplaintValidator complaintValidator;

    @Override
    @Transactional
    public ComplaintResponse createComplaint(ComplaintRequest request) {
        User currentUser = getCurrentUser();
        Category category = findActiveCategory(request.getCategoryId());
        Priority priority = findPriority(request.getPriorityId());

        Complaint complaint = Complaint.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(ComplaintStatus.SUBMITTED)
                .user(currentUser)
                .category(category)
                .priority(priority)
                .build();

        Complaint saved = complaintRepository.save(complaint);
        recordHistory(saved, null, ComplaintStatus.SUBMITTED, "Complaint submitted", currentUser);

        return complaintMapper.toResponse(findComplaintWithDetails(saved.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponse getComplaintById(Long id) {
        Complaint complaint = findComplaintWithDetails(id);
        validateViewAccess(complaint);
        return complaintMapper.toResponse(complaint);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponse getComplaintByTicketNumber(String ticketNumber) {
        Complaint complaint = complaintRepository.findByTicketNumberWithDetails(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ticket: " + ticketNumber));
        validateViewAccess(complaint);
        return complaintMapper.toResponse(complaint);
    }

    @Override
    @Transactional
    public ComplaintResponse updateComplaint(Long id, ComplaintUpdateDTO request) {
        Complaint complaint = findComplaintWithDetails(id);
        validateViewAccess(complaint);
        complaintValidator.validateAssignment(complaint.getStatus());

        if (!isStaffOrAdmin()) {
            validateOwner(complaint);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            complaint.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            complaint.setDescription(request.getDescription());
        }
        if (request.getCategoryId() != null) {
            complaint.setCategory(findActiveCategory(request.getCategoryId()));
        }
        if (request.getPriorityId() != null) {
            complaint.setPriority(findPriority(request.getPriorityId()));
        }

        Complaint saved = complaintRepository.save(complaint);
        return complaintMapper.toResponse(findComplaintWithDetails(saved.getId()));
    }

    @Override
    @Transactional
    public ComplaintResponse assignComplaint(Long id, ComplaintAssignRequest request) {
        requireStaffOrAdmin();
        Complaint complaint = findComplaintWithDetails(id);
        complaintValidator.validateAssignmentStatus(complaint.getStatus());

        User assignee = userRepository.findByIdWithRoles(request.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + request.getAssignedToId()));

        if (!isStaffOrAdminUser(assignee)) {
            throw new BadRequestException("Complaint can only be assigned to staff or admin users");
        }

        ComplaintStatus previousStatus = complaint.getStatus();
        complaint.setAssignedTo(assignee);
        complaint.setStatus(ComplaintStatus.ASSIGNED);

        Complaint saved = complaintRepository.save(complaint);
        recordHistory(saved, previousStatus, ComplaintStatus.ASSIGNED,
                request.getComment() != null ? request.getComment() : "Complaint assigned to " + assignee.getFirstName(),
                getCurrentUser());

        return complaintMapper.toResponse(findComplaintWithDetails(saved.getId()));
    }

    @Override
    @Transactional
    public ComplaintResponse updateStatus(Long id, ComplaintStatusUpdateRequest request) {
        Complaint complaint = findComplaintWithDetails(id);
        validateStatusChangeAccess(complaint, request.getStatus());

        ComplaintStatus previousStatus = complaint.getStatus();
        complaintValidator.validateStatusTransition(previousStatus, request.getStatus());
        complaintValidator.validateResolutionNotes(request.getStatus(), request.getResolutionNotes());

        complaint.setStatus(request.getStatus());

        if (request.getStatus() == ComplaintStatus.RESOLVED) {
            complaint.setResolutionNotes(request.getResolutionNotes());
            complaint.setResolvedAt(LocalDateTime.now());
        }

        Complaint saved = complaintRepository.save(complaint);
        recordHistory(saved, previousStatus, request.getStatus(), request.getComment(), getCurrentUser());

        return complaintMapper.toResponse(findComplaintWithDetails(saved.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplaintHistoryDTO> getComplaintHistory(Long id) {
        Complaint complaint = findComplaintWithDetails(id);
        validateViewAccess(complaint);

        return complaintHistoryRepository.findByComplaintIdOrderByCreatedAtDesc(id).stream()
                .map(complaintMapper::toHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ComplaintResponse> searchComplaints(
            ComplaintStatus status,
            Long categoryId,
            Long priorityId,
            Long assignedToId,
            String search,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size) {

        Specification<Complaint> spec = Specification
                .where(ComplaintSpecification.hasStatus(status))
                .and(ComplaintSpecification.hasCategoryId(categoryId))
                .and(ComplaintSpecification.hasPriorityId(priorityId))
                .and(ComplaintSpecification.hasAssignedToId(assignedToId))
                .and(ComplaintSpecification.search(search))
                .and(ComplaintSpecification.createdAfter(fromDate))
                .and(ComplaintSpecification.createdBefore(toDate));

        if (isRegularUser()) {
            spec = spec.and(ComplaintSpecification.hasUserId(SecurityUtils.getCurrentUserId()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Complaint> result = complaintRepository.findAll(spec, pageable);

        List<ComplaintResponse> content = result.getContent().stream()
                .map(complaint -> {
                    Complaint detailed = findComplaintWithDetails(complaint.getId());
                    return complaintMapper.toResponse(detailed);
                })
                .collect(Collectors.toList());

        return PagedResponse.<ComplaintResponse>builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDTO> getAssignableStaff() {
        requireStaffOrAdmin();
        return userRepository.findActiveStaffAndAdmins().stream()
                .map(complaintMapper::toUserSummary)
                .collect(Collectors.toList());
    }

    private Complaint findComplaintWithDetails(Long id) {
        return complaintRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));
    }

    private Category findActiveCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BadRequestException("Selected category is not active");
        }
        return category;
    }

    private Priority findPriority(Long priorityId) {
        return priorityRepository.findById(priorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + priorityId));
    }

    private User getCurrentUser() {
        return userRepository.findByEmailWithRoles(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private void recordHistory(Complaint complaint, ComplaintStatus previousStatus,
                               ComplaintStatus newStatus, String comment, User changedBy) {
        ComplaintHistory history = ComplaintHistory.builder()
                .complaint(complaint)
                .changedBy(changedBy)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .comment(comment)
                .build();
        complaintHistoryRepository.save(history);
    }

    private void validateViewAccess(Complaint complaint) {
        if (isStaffOrAdmin()) {
            return;
        }
        validateOwner(complaint);
    }

    private void validateOwner(Complaint complaint) {
        if (!complaint.getUser().getId().equals(SecurityUtils.getCurrentUserId())) {
            throw new AccessDeniedException("You do not have access to this complaint");
        }
    }

    private void validateStatusChangeAccess(Complaint complaint, ComplaintStatus newStatus) {
        if (newStatus == ComplaintStatus.CLOSED) {
            if (isStaffOrAdmin()) {
                return;
            }
            validateOwner(complaint);
            if (complaint.getStatus() != ComplaintStatus.RESOLVED) {
                throw new BadRequestException("Only resolved complaints can be closed");
            }
            return;
        }

        if (newStatus == ComplaintStatus.REJECTED) {
            requireStaffOrAdmin();
            return;
        }

        requireStaffOrAdmin();
    }

    private void requireStaffOrAdmin() {
        if (!isStaffOrAdmin()) {
            throw new UnauthorizedException("Staff or admin access required");
        }
    }

    private boolean isStaffOrAdmin() {
        return hasAnyRole(UserRole.ROLE_STAFF, UserRole.ROLE_ADMIN);
    }

    private boolean isRegularUser() {
        return hasAnyRole(UserRole.ROLE_USER) && !isStaffOrAdmin();
    }

    private boolean hasAnyRole(UserRole... roles) {
        CustomUserDetails details = SecurityUtils.getCurrentUserDetails();
        Set<UserRole> userRoles = details.getUser().getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        for (UserRole role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStaffOrAdminUser(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == UserRole.ROLE_STAFF || role.getName() == UserRole.ROLE_ADMIN);
    }
}
