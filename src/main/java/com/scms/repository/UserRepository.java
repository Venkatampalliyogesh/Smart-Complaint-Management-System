package com.scms.repository;

import com.scms.entity.User;
import com.scms.enums.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = { "roles" })
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = { "roles" })
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    List<User> findByActiveTrue();

    Optional<User> findByIdAndActiveTrue(Long id);

    long countByActiveTrue();

    @Query("""
            SELECT DISTINCT u
            FROM User u
            JOIN FETCH u.roles
            WHERE u.email = :email
            """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            JOIN FETCH u.roles
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            JOIN u.roles r
            WHERE r.name IN (
                com.scms.enums.UserRole.ROLE_ADMIN,
                com.scms.enums.UserRole.ROLE_STAFF
            )
            AND u.active = true
            """)
    List<User> findActiveStaffAndAdmins();

    @Query("""
            SELECT COUNT(DISTINCT u)
            FROM User u
            JOIN u.roles r
            WHERE r.name = :role
            """)
    long countByRole(@Param("role") UserRole role);

}