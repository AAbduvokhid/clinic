package uz.snow.clinic.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.patient.model.dto.request.PatientLoginRequest;
import uz.snow.clinic.patient.model.dto.request.PatientRegisterRequest;
import uz.snow.clinic.patient.model.dto.response.PatientAuthResponse;
import uz.snow.clinic.patient.service.PatientAuthService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient-auth")
public class PatientAuthController {

    private final PatientAuthService patientAuthService;

    // POST /api/v1/patient-auth/register
    // Public — no token needed
    // Patient self-registration with name, phone, password
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PatientAuthResponse>> register(
            @Valid @RequestBody PatientRegisterRequest request) {

        PatientAuthResponse response = patientAuthService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful! Welcome to MediCore.", response));
    }

    // POST /api/v1/patient-auth/login
    // Public — no token needed
    // Patient logs in with phone + password
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<PatientAuthResponse>> login(
            @Valid @RequestBody PatientLoginRequest request) {

        PatientAuthResponse response = patientAuthService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful!", response));
    }
}