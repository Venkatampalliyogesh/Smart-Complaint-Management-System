package com.scms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "password", "roles", "complaints", "assignedComplaints", "notifications" })
public class User extends BaseAuditEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Email
        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @NotBlank
        @Column(nullable = false)
        private String password;

        @NotBlank
        @Size(max = 50)
        @Column(nullable = false, length = 50)
        private String firstName;

        @NotBlank
        @Size(max = 50)
        @Column(nullable = false, length = 50)
        private String lastName;

        @Size(max = 20)
        @Column(length = 20)
        private String phone;

        @Builder.Default
        @Column(nullable = false)
        private Boolean enabled = true;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
        @Builder.Default
        private Set<Role> roles = new HashSet<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<Complaint> complaints = new ArrayList<>();

        @OneToMany(mappedBy = "assignedTo")
        @Builder.Default
        private List<Complaint> assignedComplaints = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<Notification> notifications = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<AuditLog> auditLogs = new ArrayList<>();

        @OneToMany(mappedBy = "changedBy")
        @Builder.Default
        private List<ComplaintHistory> complaintHistories = new ArrayList<>();

        public String getFullName() {
                return firstName + " " + lastName;
        }
}