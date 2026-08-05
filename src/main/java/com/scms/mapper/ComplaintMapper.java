package com.scms.mapper;

import com.scms.dto.ComplaintHistoryDTO;
import com.scms.dto.ComplaintResponse;
import com.scms.entity.Category;
import com.scms.entity.Complaint;
import com.scms.entity.ComplaintHistory;
import com.scms.entity.Priority;
import com.scms.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ComplaintMapper {

    public ComplaintResponse toResponse(Complaint complaint) {

        if (complaint == null) {
            return null;
        }

        return ComplaintResponse.builder()
                .id(complaint.getId())
                .ticketNumber(complaint.getTicketNumber())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .resolutionNotes(complaint.getResolutionNotes())
                .resolvedAt(complaint.getResolvedAt())
                .submittedBy(toUserSummary(complaint.getUser()))
                .assignedTo(toUserSummary(complaint.getAssignedTo()))
                .category(toCategory(complaint.getCategory()))
                .priority(toPriority(complaint.getPriority()))
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .build();
    }

    public ComplaintHistoryDTO toHistoryDTO(ComplaintHistory history) {

        if (history == null) {
            return null;
        }

        return ComplaintHistoryDTO.builder()
                .id(history.getId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .comment(history.getComment())
                .changedBy(toUserSummary(history.getChangedBy()))
                .createdAt(history.getCreatedAt())
                .build();
    }

    private ComplaintResponse.UserSummaryDTO toUserSummary(User user) {

        if (user == null) {
            return null;
        }

        return ComplaintResponse.UserSummaryDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private ComplaintResponse.CategoryDTO toCategory(Category category) {

        if (category == null) {
            return null;
        }

        return ComplaintResponse.CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private ComplaintResponse.PriorityDTO toPriority(Priority priority) {

        if (priority == null) {
            return null;
        }

        return ComplaintResponse.PriorityDTO.builder()
                .id(priority.getId())
                .name(priority.getName().name())
                .level(priority.getLevel())
                .build();
    }

}