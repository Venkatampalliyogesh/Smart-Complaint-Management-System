package com.scms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Boolean enabled;

    private Set<String> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}