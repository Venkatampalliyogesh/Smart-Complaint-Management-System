package com.scms.service.impl;

import com.scms.dto.ChangePasswordRequest;
import com.scms.dto.ForgotPasswordRequest;
import com.scms.dto.ProfileDTO;
import com.scms.dto.ResetPasswordRequest;
import com.scms.dto.UpdateProfileRequest;
import com.scms.dto.UserDTO;
import com.scms.entity.PasswordResetToken;
import com.scms.entity.User;
import com.scms.exception.BadRequestException;
import com.scms.exception.InvalidCredentialsException;
import com.scms.exception.ResourceNotFoundException;
import com.scms.exception.UnauthorizedException;
import com.scms.mapper.UserMapper;
import com.scms.repository.PasswordResetTokenRepository;
import com.scms.repository.RefreshTokenRepository;
import com.scms.repository.UserRepository;
import com.scms.security.SecurityUtils;
import com.scms.service.EmailService;
import com.scms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.password-reset.expiration:3600000}")
    private long passwordResetExpiration;

    @Override
    @Transactional(readOnly = true)
    public ProfileDTO getCurrentUserProfile() {
        User user = findCurrentUserWithRoles();
        return userMapper.toProfileDto(user);
    }

    @Override
    @Transactional
    public ProfileDTO updateCurrentUserProfile(UpdateProfileRequest request) {
        User user = findCurrentUserWithRoles();
        userMapper.updateEntityFromRequest(user, request.getFirstName(), request.getLastName(), request.getPhone());
        User savedUser = userRepository.save(user);
        return userMapper.toProfileDto(savedUser);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = findCurrentUserWithRoles();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenRepository.deleteByUserId(user.getId());
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            String tokenValue = UUID.randomUUID().toString();
            LocalDateTime expiryDate = calculateExpiryDate();
            PasswordResetToken resetToken = passwordResetTokenRepository.findByUserId(user.getId())
                    .map(existing -> {
                        existing.setToken(tokenValue);
                        existing.setExpiryDate(expiryDate);
                        existing.setUsed(false);
                        return existing;
                    })
                    .orElseGet(() -> PasswordResetToken.builder()
                            .user(user)
                            .token(tokenValue)
                            .expiryDate(expiryDate)
                            .used(false)
                            .build());

            passwordResetTokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), tokenValue);
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired reset token"));

        if (Boolean.TRUE.equals(resetToken.getUsed())
                || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Invalid or expired reset token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        refreshTokenRepository.deleteByUserId(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    private User findCurrentUserWithRoles() {
        String email = SecurityUtils.getCurrentUserEmail();
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + email));
    }
}
