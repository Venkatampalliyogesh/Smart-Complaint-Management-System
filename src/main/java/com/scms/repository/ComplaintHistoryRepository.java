package com.scms.repository;

import com.scms.entity.ComplaintHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory, Long> {

    @EntityGraph(attributePaths = {
            "complaint",
            "changedBy"
    })
    List<ComplaintHistory> findByComplaintIdOrderByCreatedAtDesc(Long complaintId);

    @EntityGraph(attributePaths = {
            "complaint",
            "changedBy"
    })
    List<ComplaintHistory> findByChangedById(Long changedById);

}