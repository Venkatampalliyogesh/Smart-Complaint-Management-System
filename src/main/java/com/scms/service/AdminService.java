package com.scms.service;

import com.scms.dto.DashboardDTO;
import com.scms.dto.UserDTO;

import java.util.List;

public interface AdminService {

    DashboardDTO getDashboardStatistics();

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    UserDTO updateUserStatus(Long userId, Boolean enabled);

    UserDTO updateUserRole(Long userId, String roleName);

    void deleteUser(Long userId);

}