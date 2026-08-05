package com.scms.controller;

import com.scms.dto.ChangePasswordRequest;
import com.scms.dto.ProfileDTO;
import com.scms.dto.UpdateProfileRequest;
import com.scms.service.UserService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile Management")
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get User Profile")
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile(
            @PathVariable Long userId) {

        ProfileDTO profile = userService.getProfile(userId);

        return ResponseEntity.ok(
                ApiResponse.<ProfileDTO>builder()
                        .success(true)
                        .message("Profile Retrieved Successfully")
                        .data(profile)
                        .build());
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update User Profile")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {

        ProfileDTO profile = userService.updateProfile(userId, request);

        return ResponseEntity.ok(
                ApiResponse.<ProfileDTO>builder()
                        .success(true)
                        .message("Profile Updated Successfully")
                        .data(profile)
                        .build());
    }

    @PatchMapping("/{userId}/change-password")
    @Operation(summary = "Change Password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(userId, request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Password Changed Successfully")
                        .build());
    }

}