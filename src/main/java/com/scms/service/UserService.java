package com.scms.service;

import com.scms.dto.ChangePasswordRequest;
import com.scms.dto.ForgotPasswordRequest;
import com.scms.dto.ProfileDTO;
import com.scms.dto.ResetPasswordRequest;
import com.scms.dto.UpdateProfileRequest;
import com.scms.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    ProfileDTO getProfile(Long userId);

    ProfileDTO updateProfile(Long userId, UpdateProfileRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    UserDTO enableUser(Long userId);

    UserDTO disableUser(Long userId);

    void deleteUser(Long userId);

}