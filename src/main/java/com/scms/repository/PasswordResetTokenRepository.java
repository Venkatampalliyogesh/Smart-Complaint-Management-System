package com.scms.repository;

import com.scms.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query("SELECT prt FROM PasswordResetToken prt JOIN FETCH prt.user u LEFT JOIN FETCH u.roles WHERE prt.token = :token")
    Optional<PasswordResetToken> findByToken(@Param("token") String token);

    Optional<PasswordResetToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
