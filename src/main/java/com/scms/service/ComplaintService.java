package com.scms.service;

import com.scms.dto.ComplaintAssignRequest;
import com.scms.dto.ComplaintHistoryDTO;
import com.scms.dto.ComplaintRequest;
import com.scms.dto.ComplaintResponse;
import com.scms.dto.ComplaintStatusUpdateRequest;
import com.scms.dto.ComplaintUpdateDTO;
import com.scms.dto.PagedResponse;
import com.scms.dto.UserSummaryDTO;
import com.scms.enums.ComplaintStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplaintService {

    ComplaintResponse createComplaint(ComplaintRequest request);

    PagedResponse<ComplaintResponse> searchComplaints(
            ComplaintStatus status,
            Long categoryId,
            Long priorityId,
            Long assignedToId,
            String search,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            int page,
            int size);

    ComplaintResponse getComplaintById(Long complaintId);

    ComplaintResponse getComplaintByTicketNumber(String ticketNumber);

    ComplaintResponse updateComplaint(
            Long complaintId,
            ComplaintUpdateDTO request);

    ComplaintResponse assignComplaint(
            Long complaintId,
            ComplaintAssignRequest request);

    ComplaintResponse updateStatus(
            Long complaintId,
            ComplaintStatusUpdateRequest request);

    List<ComplaintHistoryDTO> getComplaintHistory(Long complaintId);

    List<UserSummaryDTO> getAssignableStaff();

}