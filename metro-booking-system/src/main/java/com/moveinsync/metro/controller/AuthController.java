package com.moveinsync.metro.controller;

import com.moveinsync.metro.dto.AuthLoginRequestDTO;
import com.moveinsync.metro.dto.AuthResponseDTO;
import com.moveinsync.metro.dto.AuthSignupRequestDTO;
import com.moveinsync.metro.security.JwtService;
import com.moveinsync.metro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            String role = user.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");
            String requestedRole = request.getRole().trim().toUpperCase();
            if (!role.equalsIgnoreCase(requestedRole)) {
                throw new BadCredentialsException("Selected role does not match your account role");
            }
            return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer", role));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody AuthSignupRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok(Map.of("message", "Signup successful. Please login."));
    }
}
