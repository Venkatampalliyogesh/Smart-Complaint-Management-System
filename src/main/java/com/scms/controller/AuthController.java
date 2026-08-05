package com.scms.controller;

import com.scms.dto.LoginRequest;
import com.scms.dto.LoginResponse;
import com.scms.dto.RegisterRequest;
import com.scms.service.AuthService;
import com.scms.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register New User")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        LoginResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("User Registered Successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    @Operation(summary = "User Login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Login Successful")
                        .data(response)
                        .build());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT Token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @RequestParam String refreshToken) {

        LoginResponse response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Token Refreshed Successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout User")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestParam Long userId) {

        authService.logout(userId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logout Successful")
                        .build());
    }

}