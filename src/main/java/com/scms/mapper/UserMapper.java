package com.scms.mapper;

import com.scms.dto.ProfileDTO;
import com.scms.dto.UserDTO;
import com.scms.entity.Role;
import com.scms.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {

        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.getEnabled())
                .roles(getRoles(user))
                .build();
    }

    public ProfileDTO toProfileDTO(User user) {

        if (user == null) {
            return null;
        }

        return ProfileDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.getEnabled())
                .roles(getRoles(user))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public void updateUser(User user, ProfileDTO profileDTO) {

        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setPhone(profileDTO.getPhone());

    }

    private Set<String> getRoles(User user) {

        if (user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles()
                .stream()
                .map(Role::getName)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

}