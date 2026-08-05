package com.scms.controller;

import com.scms.dto.ComplaintHistoryDTO;
import com.scms.dto.ComplaintRequest;
import com.scms.dto.ComplaintResponse;
import com.scms.dto.ComplaintUpdateDTO;
import com.scms.service.ComplaintService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@Tag(name = "Complaint Management")
@SecurityRequirement(name = "Bearer Authentication")
public class ComplaintController {

        private final ComplaintService complaintService;

        @PostMapping
        @Operation(summary = "Create Complaint")
        public ResponseEntity<ApiResponse<ComplaintResponse>> createComplaint(
                        @Valid @RequestBody ComplaintRequest request) {

                ComplaintResponse response = complaintService.createComplaint(request);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.<ComplaintResponse>builder()
                                                                .success(true)
                                                                .message("Complaint Submitted Successfully")
                                                                .data(response)
                                                                .build());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get Complaint By Id")
        public ResponseEntity<ApiResponse<ComplaintResponse>> getComplaint(
                        @PathVariable Long id) {

                ComplaintResponse response = complaintService.getComplaintById(id);

                return ResponseEntity.ok(
                                ApiResponse.<ComplaintResponse>builder()
                                                .success(true)
                                                .message("Complaint Retrieved Successfully")
                                                .data(response)
                                                .build());
        }

        @GetMapping("/ticket/{ticketNumber}")
        @Operation(summary = "Track Complaint")
        public ResponseEntity<ApiResponse<ComplaintResponse>> trackComplaint(
                        @PathVariable String ticketNumber) {

                ComplaintResponse response = complaintService.getComplaintByTicketNumber(ticketNumber);

                return ResponseEntity.ok(
                                ApiResponse.<ComplaintResponse>builder()
                                                .success(true)
                                                .message("Complaint Found")
                                                .data(response)
                                                .build());
        }

        @GetMapping("/user/{userId}")
        @Operation(summary = "Get User Complaints")
        public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getUserComplaints(
                        @PathVariable Long userId) {

                List<ComplaintResponse> complaints = complaintService.getComplaintsByUser(userId);

                return ResponseEntity.ok(
                                ApiResponse.<List<ComplaintResponse>>builder()
                                                .success(true)
                                                .message("Complaints Retrieved Successfully")
                                                .data(complaints)
                                                .build());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update Complaint")
        public ResponseEntity<ApiResponse<ComplaintResponse>> updateComplaint(
                        @PathVariable Long id,
                        @Valid @RequestBody ComplaintUpdateDTO request) {

                ComplaintResponse response = complaintService.updateComplaint(id, request);

                return ResponseEntity.ok(
                                ApiResponse.<ComplaintResponse>builder()
                                                .success(true)
                                                .message("Complaint Updated Successfully")
                                                .data(response)
                                                .build());
        }

        @PatchMapping("/{id}/status")
        @Operation(summary = "Update Complaint Status")
        public ResponseEntity<ApiResponse<ComplaintResponse>> updateStatus(
                        @PathVariable Long id,
                        @RequestParam String status) {

                ComplaintResponse response = complaintService.updateComplaintStatus(id, status);

                return ResponseEntity.ok(
                                ApiResponse.<ComplaintResponse>builder()
                                                .success(true)
                                                .message("Complaint Status Updated Successfully")
                                                .data(response)
                                                .build());
        }

        @PatchMapping("/{id}/assign/{staffId}")
        @Operation(summary = "Assign Complaint")
        public ResponseEntity<ApiResponse<ComplaintResponse>> assignComplaint(
                        @PathVariable Long id,
                        @PathVariable Long staffId) {

                ComplaintResponse response = complaintService.assignComplaint(id, staffId);

                return ResponseEntity.ok(
                                ApiResponse.<ComplaintResponse>builder()
                                                .success(true)
                                                .message("Complaint Assigned Successfully")
                                                .data(response)
                                                .build());
        }

        @GetMapping("/{id}/history")
        @Operation(summary = "Complaint History")
        public ResponseEntity<ApiResponse<List<ComplaintHistoryDTO>>> getHistory(
                        @PathVariable Long id) {

                List<ComplaintHistoryDTO> history = complaintService.getComplaintHistory(id);

                return ResponseEntity.ok(
                                ApiResponse.<List<ComplaintHistoryDTO>>builder()
                                                .success(true)
                                                .message("Complaint History Retrieved Successfully")
                                                .data(history)
                                                .build());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete Complaint")
        public ResponseEntity<ApiResponse<Void>> deleteComplaint(
                        @PathVariable Long id) {

                complaintService.deleteComplaint(id);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("Complaint Deleted Successfully")
                                                .build());
        }

}