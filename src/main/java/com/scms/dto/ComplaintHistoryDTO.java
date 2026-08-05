package com.scms.dto;

import com.scms.enums.ComplaintStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintHistoryDTO {

    private Long id;

    private ComplaintStatus previousStatus;

    private ComplaintStatus newStatus;

    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String comment;

    private UserSummaryDTO changedBy;

    private LocalDateTime createdAt;

}