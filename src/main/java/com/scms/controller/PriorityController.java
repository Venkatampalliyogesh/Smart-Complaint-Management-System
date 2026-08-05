package com.scms.controller;

import com.scms.dto.PriorityDTO;
import com.scms.service.PriorityService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
@RequiredArgsConstructor
@Tag(name = "Priorities", description = "Complaint priority lookup APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    @Operation(summary = "List all complaint priorities")
    public ResponseEntity<ApiResponse<List<PriorityDTO>>> getAllPriorities() {
        List<PriorityDTO> priorities = priorityService.getAllPriorities();
        return ResponseEntity.ok(ApiResponse.<List<PriorityDTO>>builder()
                .success(true)
                .message("Priorities retrieved successfully")
                .data(priorities)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get priority by ID")
    public ResponseEntity<ApiResponse<PriorityDTO>> getPriorityById(@PathVariable Long id) {
        PriorityDTO priority = priorityService.getPriorityById(id);
        return ResponseEntity.ok(ApiResponse.<PriorityDTO>builder()
                .success(true)
                .message("Priority retrieved successfully")
                .data(priority)
                .build());
    }
}
