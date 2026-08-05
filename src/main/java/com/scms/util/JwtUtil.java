package com.scms.util;

import com.scms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public String generateToken(UserDetails userDetails) {
        return jwtTokenProvider.generateToken(userDetails);
    }

    public String extractUsername(String token) {
        return jwtTokenProvider.extractUsername(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenProvider.validateToken(token, userDetails);
    }

    public Long getExpirationTime() {
        return jwtTokenProvider.getExpirationTime();
    }

}