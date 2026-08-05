package com.scms.controller;

import com.scms.dto.DashboardDTO;
import com.scms.dto.UserDTO;
import com.scms.service.AdminService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

        private final AdminService adminService;

        @GetMapping("/dashboard")
        @Operation(summary = "Admin Dashboard")
        public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {

                DashboardDTO dashboard = adminService.getDashboardStatistics();

                return ResponseEntity.ok(
                                ApiResponse.<DashboardDTO>builder()
                                                .success(true)
                                                .message("Dashboard Loaded Successfully")
                                                .data(dashboard)
                                                .build());
        }

        @GetMapping("/users")
        @Operation(summary = "Get All Users")
        public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {

                List<UserDTO> users = adminService.getAllUsers();

                return ResponseEntity.ok(
                                ApiResponse.<List<UserDTO>>builder()
                                                .success(true)
                                                .message("Users Retrieved Successfully")
                                                .data(users)
                                                .build());
        }

        @GetMapping("/users/{id}")
        @Operation(summary = "Get User By Id")
        public ResponseEntity<ApiResponse<UserDTO>> getUser(
                        @PathVariable Long id) {

                UserDTO user = adminService.getUserById(id);

                return ResponseEntity.ok(
                                ApiResponse.<UserDTO>builder()
                                                .success(true)
                                                .message("User Retrieved Successfully")
                                                .data(user)
                                                .build());
        }

        @PutMapping("/users/{id}")
        @Operation(summary = "Update User")
        public ResponseEntity<ApiResponse<UserDTO>> updateUser(
                        @PathVariable Long id,
                        @Valid @RequestBody UserDTO userDTO) {

                UserDTO user = adminService.updateUser(id, userDTO);

                return ResponseEntity.ok(
                                ApiResponse.<UserDTO>builder()
                                                .success(true)
                                                .message("User Updated Successfully")
                                                .data(user)
                                                .build());
        }

        @PatchMapping("/users/{id}/status")
        @Operation(summary = "Enable / Disable User")
        public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
                        @PathVariable Long id,
                        @RequestParam Boolean enabled) {

                UserDTO user = adminService.updateUserStatus(id, enabled);

                return ResponseEntity.ok(
                                ApiResponse.<UserDTO>builder()
                                                .success(true)
                                                .message("User Status Updated Successfully")
                                                .data(user)
                                                .build());
        }

        @DeleteMapping("/users/{id}")
        @Operation(summary = "Delete User")
        public ResponseEntity<ApiResponse<Void>> deleteUser(
                        @PathVariable Long id) {

                adminService.deleteUser(id);

                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("User Deleted Successfully")
                                                .build());
        }

}