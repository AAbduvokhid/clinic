package uz.snow.clinic.visit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.user.model.dto.response.ApiResponse;
import uz.snow.clinic.user.repository.UserRepository;
import uz.snow.clinic.visit.model.dto.request.RegisterVisitRequest;
import uz.snow.clinic.visit.model.dto.response.VisitResponse;

import uz.snow.clinic.visit.model.enums.PaymentStatus;
import uz.snow.clinic.visit.service.VisitService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;
    private final UserRepository userRepository;

    // GET /api/v1/visits
    @GetMapping
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAll() {
        List<VisitResponse> visits = visitService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visits));
    }

    // GET /api/v1/visits/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VisitResponse>> findById(
            @PathVariable Long id) {
        VisitResponse visit = visitService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Visit fetched successfully", visit));
    }

    // GET /api/v1/visits/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByPatient(
            @PathVariable Long patientId) {
        List<VisitResponse> visits = visitService.findAllByPatient(patientId);
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visits));
    }

    // GET /api/v1/visits/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByDoctor(
            @PathVariable Long doctorId) {
        List<VisitResponse> visits = visitService.findAllByDoctor(doctorId);
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visits));
    }

    // GET /api/v1/visits/payment-status/{status}
    @GetMapping("/payment-status/{status}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByPaymentStatus(
            @PathVariable PaymentStatus status) {
        List<VisitResponse> visits = visitService.findAllByPaymentStatus(status);
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visits));
    }

    // GET /api/v1/visits/date-range?start=...&end=...
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        List<VisitResponse> visits = visitService.findAllByDateRange(start, end);
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visits));
    }

    // POST /api/v1/visits
    @PostMapping
    public ResponseEntity<ApiResponse<VisitResponse>> register(
            @Valid @RequestBody RegisterVisitRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long registeredBy = userRepository
                .findByUsername(userDetails.getUsername())
                .map(u -> u.getId())
                .orElse(1L);

        VisitResponse saved = visitService.register(request, registeredBy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Visit registered successfully", saved));
    }

    // PATCH /api/v1/visits/{id}/payment
    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<VisitResponse>> updatePayment(
            @PathVariable Long id,
            @RequestParam BigDecimal paidAmount,
            @RequestParam PaymentStatus paymentStatus) {
        VisitResponse updated = visitService.updatePayment(id, paidAmount, paymentStatus);
        return ResponseEntity.ok(
                ApiResponse.success("Payment updated successfully", updated));
    }

    // DELETE /api/v1/visits/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}