package com.scms.repository;

import com.scms.entity.Complaint;
import com.scms.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>,
                JpaSpecificationExecutor<Complaint> {

        @EntityGraph(attributePaths = {
                        "user",
                        "assignedTo",
                        "category",
                        "priority"
        })
        Optional<Complaint> findByIdWithDetails(Long id);

        @EntityGraph(attributePaths = {
                        "user",
                        "assignedTo",
                        "category",
                        "priority"
        })
        Optional<Complaint> findByTicketNumberWithDetails(String ticketNumber);

        List<Complaint> findByUserId(Long userId);

        List<Complaint> findByAssignedToId(Long assignedToId);

        List<Complaint> findByStatus(ComplaintStatus status);

        List<Complaint> findByCategoryId(Long categoryId);

        List<Complaint> findByPriorityId(Long priorityId);

        List<Complaint> findByUserIdAndStatus(Long userId,
                        ComplaintStatus status);

        long countByStatus(ComplaintStatus status);

        long countByUserId(Long userId);

        long countByAssignedToId(Long assignedToId);

        long countByUserIdAndStatus(Long userId,
                        ComplaintStatus status);

}