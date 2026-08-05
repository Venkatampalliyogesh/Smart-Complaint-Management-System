package com.scms.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    /*
     * =======================
     * USER DASHBOARD
     * =======================
     */

    private Long totalComplaints;

    private Long submittedComplaints;

    private Long assignedComplaints;

    private Long inProgressComplaints;

    private Long resolvedComplaints;

    private Long closedComplaints;

    /*
     * =======================
     * ADMIN DASHBOARD
     * =======================
     */

    private Long totalUsers;

    private Long activeUsers;

    private Long totalAdmins;

    private Long totalStaff;

    private Long totalCategories;

    private Long totalNotifications;

    /*
     * =======================
     * CHARTS
     * =======================
     */

    private Map<String, Long> complaintsByStatus;

    private Map<String, Long> complaintsByPriority;

    private Map<String, Long> complaintsByCategory;

    private Map<String, Long> usersByRole;

    /*
     * =======================
     * RECENT DATA
     * =======================
     */

    private List<ComplaintResponse> recentComplaints;

    private List<NotificationDTO> recentNotifications;

}