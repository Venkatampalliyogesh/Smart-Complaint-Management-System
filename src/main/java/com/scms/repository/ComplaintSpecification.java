package com.scms.repository;

import com.scms.entity.Complaint;
import com.scms.enums.ComplaintStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public final class ComplaintSpecification {

    private ComplaintSpecification() {
    }

    public static Specification<Complaint> hasStatus(ComplaintStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Complaint> hasCategoryId(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? cb.conjunction() : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Complaint> hasPriorityId(Long priorityId) {
        return (root, query, cb) ->
                priorityId == null ? cb.conjunction() : cb.equal(root.get("priority").get("id"), priorityId);
    }

    public static Specification<Complaint> hasUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? cb.conjunction() : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Complaint> hasAssignedToId(Long assignedToId) {
        return (root, query, cb) ->
                assignedToId == null ? cb.conjunction() : cb.equal(root.get("assignedTo").get("id"), assignedToId);
    }

    public static Specification<Complaint> search(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("ticketNumber")), pattern)
            );
        };
    }

    public static Specification<Complaint> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) ->
                fromDate == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }

    public static Specification<Complaint> createdBefore(LocalDateTime toDate) {
        return (root, query, cb) ->
                toDate == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
    }
}
