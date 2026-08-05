package com.scms.dto;

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
public class ComplaintAssignRequest {

    @NotNull(message = "Staff member is required")
    private Long assignedToId;

    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String comment;

}