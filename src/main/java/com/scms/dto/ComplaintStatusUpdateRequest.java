package com.scms.dto;

import com.scms.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintStatusUpdateRequest {

    @NotNull(message = "Complaint status is required")
    private ComplaintStatus status;

    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String comment;

    @Size(max = 2000, message = "Resolution notes cannot exceed 2000 characters")
    private String resolutionNotes;

}