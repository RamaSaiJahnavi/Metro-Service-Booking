package com.moveinsync.metro.service;

import com.moveinsync.metro.dto.AuthSignupRequestDTO;
import com.moveinsync.metro.entity.AppUser;
import com.moveinsync.metro.entity.UserRole;
import com.moveinsync.metro.exception.CustomException;
import com.moveinsync.metro.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.pass-key}")
    private String adminPassKey;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(AuthSignupRequestDTO request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (appUserRepository.existsByUsername(username)) {
            throw new CustomException("Username already exists", "USERNAME_EXISTS");
        }

        if (appUserRepository.existsByEmail(email)) {
            throw new CustomException("Email already exists", "EMAIL_EXISTS");
        }

        UserRole role = parseRole(request.getRole());
        if (role == UserRole.ADMIN) {
            if (request.getAdminPassKey() == null || !adminPassKey.equals(request.getAdminPassKey().trim())) {
                throw new CustomException("Invalid admin pass key", "INVALID_ADMIN_PASS_KEY");
            }
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        appUserRepository.save(user);
    }

    private UserRole parseRole(String roleValue) {
        try {
            return UserRole.valueOf(roleValue.trim().toUpperCase());
        } catch (Exception ex) {
            throw new CustomException("Role must be USER or ADMIN", "INVALID_ROLE");
        }
    }
}
