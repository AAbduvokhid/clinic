package uz.snow.clinic.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.auth.dto.request.LoginRequest;
import uz.snow.clinic.auth.dto.response.LoginResponse;
import uz.snow.clinic.auth.service.AuthService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
    @GetMapping("/test-password")
    public String testPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$slYQmyNdgTY18LGvgxPwsOt3wgVznajV.WMijNlPOYWgW1yNzc0Du";
        boolean matches = encoder.matches("admin123", hash);
        return "Password matches: " + matches;
    }
}