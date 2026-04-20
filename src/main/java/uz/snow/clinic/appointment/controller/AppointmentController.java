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
import uz.snow.clinic.appointment.model.dto.request.PatientAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.request.RegisterAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.request.UpdateAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.response.AppointmentResponse;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;
import uz.snow.clinic.appointment.service.AppointmentService;
import uz.snow.clinic.common.exception.BaseException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.patient.repository.PatientRepository;
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
    private final PatientRepository patientRepository;

    // GET /api/v1/appointments
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully", appointmentService.findAll()));
    }

    // GET /api/v1/appointments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointment fetched successfully", appointmentService.findById(id)));
    }

    // GET /api/v1/appointments/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully", appointmentService.findAllByPatient(patientId)));
    }

    // GET /api/v1/appointments/my — patient gets their OWN appointments using their token
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {

        // Find user from token
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Find patient linked to this user
        Long patientId = patientRepository.findByUserId(userId)
                .map(p -> p.getId())
                .orElseThrow(() -> new BaseException("No patient record linked to your account"));

        return ResponseEntity.ok(
                ApiResponse.success("Your appointments fetched successfully",
                        appointmentService.findAllByPatient(patientId)));
    }

    // GET /api/v1/appointments/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully", appointmentService.findAllByDoctor(doctorId)));
    }

    // GET /api/v1/appointments/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully", appointmentService.findAllByStatus(status)));
    }

    // GET /api/v1/appointments/date-range
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> findAllByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointments fetched successfully",
                        appointmentService.findAllByDateRange(start, end)));
    }

    // POST /api/v1/appointments — staff/admin creates appointment
    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> register(
            @Valid @RequestBody RegisterAppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long registeredBy = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId()).orElse(1L);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment registered successfully",
                        appointmentService.register(request, registeredBy)));
    }

    // POST /api/v1/appointments/book — patient books their OWN appointment
    @PostMapping("/book")
    public ResponseEntity<ApiResponse<AppointmentResponse>> book(
            @Valid @RequestBody PatientAppointmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get user from token
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Get patient linked to this user
        Long patientId = patientRepository.findByUserId(userId)
                .map(p -> p.getId())
                .orElseThrow(() -> new BaseException("No patient record linked to your account. Please contact the clinic."));

        Long registeredBy = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId()).orElse(1L);

        // Build full request from patient's data
        RegisterAppointmentRequest fullRequest = new RegisterAppointmentRequest();
        fullRequest.setPatientId(patientId);
        fullRequest.setDoctorId(request.getDoctorId());
        fullRequest.setDepartmentId(request.getDepartmentId());
        fullRequest.setAppointmentDate(request.getAppointmentDate());
        fullRequest.setNotes(request.getNotes());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully!",
                        appointmentService.register(fullRequest, registeredBy)));
    }

    // PUT /api/v1/appointments
    @PutMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Appointment updated successfully", appointmentService.update(request)));
    }

    // PATCH /api/v1/appointments/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully"));
    }

    // DELETE /api/v1/appointments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}