package uz.snow.clinic.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.appointment.model.dto.request.RegisterAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.request.UpdateAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.response.AppointmentResponse;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;
import uz.snow.clinic.appointment.service.AppointmentService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;
import uz.snow.clinic.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAll() {
        List<AppointmentResponse> appointments = appointmentService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> findById(
            @PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointment));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByPatientId(@PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.findAllByPatient(patientId);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointments)
        );
    }

    // GET /api/v1/appointments/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByDoctor(
            @PathVariable Long doctorId) {
        List<AppointmentResponse> appointments =
                appointmentService.findAllByDoctor(doctorId);
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully", appointments));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByStatus(
            @PathVariable AppointmentStatus status) {
        List<AppointmentResponse> appointments = appointmentService.findAllByStatus(status);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointments)
        );
    }

    @GetMapping("/data-range")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByDataRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        List<AppointmentResponse> appointments = appointmentService.findAllByDataRange(start, end);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> register(
            @Valid @RequestBody RegisterAppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Long registeredBy = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId())
                .orElse(1L);

        AppointmentResponse saved = appointmentService.register(request, registeredBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Appointment registered succsessfully ", saved));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @Valid @RequestBody UpdateAppointmentRequest request) {
        AppointmentResponse updated = appointmentService.update(request);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment updated successfully", updated));
    }

    @PatchMapping("/{id}/cencel")
    public ResponseEntity<ApiResponse<Void>> cencel(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment canceled successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

