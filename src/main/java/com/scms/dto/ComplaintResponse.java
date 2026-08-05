package com.scms.dto;

import com.scms.enums.ComplaintStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponse {

    private Long id;

    private String ticketNumber;

    private String title;

    private String description;

    private ComplaintStatus status;

    private String resolutionNotes;

    private LocalDateTime resolvedAt;

    private UserSummaryDTO submittedBy;

    private UserSummaryDTO assignedTo;

    private CategoryDTO category;

    private PriorityDTO priority;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummaryDTO {

        private Long id;

        private String firstName;

        private String lastName;

        private String email;

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryDTO {

        private Long id;

        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriorityDTO {

        private Long id;

        private String name;

        private Integer level;
    }

}