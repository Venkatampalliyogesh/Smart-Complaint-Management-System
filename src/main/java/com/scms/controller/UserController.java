package com.scms.controller;

import com.scms.dto.ProfileDTO;
import com.scms.dto.UserDTO;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get User By Id")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @PathVariable Long id) {

        UserDTO user = userService.getUserById(id);

        return ResponseEntity.ok(
                ApiResponse.<UserDTO>builder()
                        .success(true)
                        .message("User Retrieved Successfully")
                        .data(user)
                        .build());
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get User Profile")
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile(
            @PathVariable Long id) {

        ProfileDTO profile = userService.getProfile(id);

        return ResponseEntity.ok(
                ApiResponse.<ProfileDTO>builder()
                        .success(true)
                        .message("Profile Retrieved Successfully")
                        .data(profile)
                        .build());
    }

    @PutMapping("/profile/{id}")
    @Operation(summary = "Update User Profile")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProfileDTO profileDTO) {

        ProfileDTO profile = userService.updateProfile(id, profileDTO);

        return ResponseEntity.ok(
                ApiResponse.<ProfileDTO>builder()
                        .success(true)
                        .message("Profile Updated Successfully")
                        .data(profile)
                        .build());
    }

}