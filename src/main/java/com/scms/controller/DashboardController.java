package com.scms.controller;

import com.scms.dto.DashboardDTO;
import com.scms.service.DashboardService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "User Dashboard")
    public ResponseEntity<ApiResponse<DashboardDTO>> getUserDashboard(
            @PathVariable Long userId) {

        DashboardDTO dashboard = dashboardService.getUserDashboard(userId);

        return ResponseEntity.ok(
                ApiResponse.<DashboardDTO>builder()
                        .success(true)
                        .message("User Dashboard Loaded Successfully")
                        .data(dashboard)
                        .build());
    }

    @GetMapping("/admin")
    @Operation(summary = "Admin Dashboard")
    public ResponseEntity<ApiResponse<DashboardDTO>> getAdminDashboard() {

        DashboardDTO dashboard = dashboardService.getAdminDashboard();

        return ResponseEntity.ok(
                ApiResponse.<DashboardDTO>builder()
                        .success(true)
                        .message("Admin Dashboard Loaded Successfully")
                        .data(dashboard)
                        .build());
    }

}