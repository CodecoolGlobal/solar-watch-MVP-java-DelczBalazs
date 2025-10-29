package com.codecool.solarwatch.security;

import com.codecool.solarwatch.dto.auth.AuthResponse;
import com.codecool.solarwatch.dto.auth.LoginRequest;
import com.codecool.solarwatch.dto.auth.RegisterRequest;
import com.codecool.solarwatch.entity.user.Role;
import com.codecool.solarwatch.entity.user.User;
import com.codecool.solarwatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public void register(RegisterRequest req) {
        // normalize if you want (optional but recommended)
        final String email = req.getEmail().trim();

        // fast path: app-level check
        if (users.existsByEmail(email)) {
            System.out.println("Email already registered");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        try {
            var encoded = encoder.encode(req.getPassword());
            var user = User.create(email, encoded, req.getFullName(), Set.of(Role.USER));
            users.save(user);
        } catch (DataIntegrityViolationException dup) {
            // Safety net when DB unique constraint triggers (two parallel requests)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
    }

    public AuthResponse login(LoginRequest req) {
        var token = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        try {
            authManager.authenticate(token);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return new AuthResponse(jwt.generateToken(req.getEmail()));
    }
}
