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
import uz.snow.clinic.common.exception.BaseException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.patient.repository.PatientRepository;
import uz.snow.clinic.user.model.dto.response.ApiResponse;
import uz.snow.clinic.user.repository.UserRepository;
import uz.snow.clinic.visit.model.dto.request.RegisterVisitRequest;
import uz.snow.clinic.visit.model.dto.request.UpdateVisitResultRequest;
import uz.snow.clinic.visit.model.dto.response.VisitResponse;
import uz.snow.clinic.visit.model.dto.response.VisitResultResponse;
import uz.snow.clinic.visit.model.enums.PaymentStatus;
import uz.snow.clinic.visit.service.VisitService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    // GET /api/v1/visits
    @GetMapping
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visitService.findAll()));
    }

    // GET /api/v1/visits/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VisitResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Visit fetched successfully", visitService.findById(id)));
    }

    // GET /api/v1/visits/my — patient gets their OWN visits using token
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findMyVisits(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Long patientId = patientRepository.findByUserId(userId)
                .map(p -> p.getId())
                .orElseThrow(() -> new BaseException("No patient record linked to your account"));

        return ResponseEntity.ok(
                ApiResponse.success("Your visits fetched successfully",
                        visitService.findAllByPatient(patientId)));
    }

    // GET /api/v1/visits/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visitService.findAllByPatient(patientId)));
    }

    // GET /api/v1/visits/doctor/{doctorId}
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visitService.findAllByDoctor(doctorId)));
    }

    // GET /api/v1/visits/payment-status/{status}
    @GetMapping("/payment-status/{status}")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByPaymentStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully", visitService.findAllByPaymentStatus(status)));
    }

    // GET /api/v1/visits/date-range
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<VisitResponse>>> findAllByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(
                ApiResponse.success("Visits fetched successfully",
                        visitService.findAllByDateRange(start, end)));
    }

    // POST /api/v1/visits
    @PostMapping
    public ResponseEntity<ApiResponse<VisitResponse>> register(
            @Valid @RequestBody RegisterVisitRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long registeredBy = userRepository.findByUsername(userDetails.getUsername())
                .map(u -> u.getId()).orElse(1L);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Visit registered successfully",
                        visitService.register(request, registeredBy)));
    }

    // PATCH /api/v1/visits/{id}/payment
    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<VisitResponse>> updatePayment(
            @PathVariable Long id,
            @RequestParam BigDecimal paidAmount,
            @RequestParam PaymentStatus paymentStatus) {
        return ResponseEntity.ok(
                ApiResponse.success("Payment updated successfully",
                        visitService.updatePayment(id, paidAmount, paymentStatus)));
    }

    // DELETE /api/v1/visits/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // ── Add these endpoints to your existing VisitController.java ──
// Add these imports:
// import uz.snow.clinic.visit.model.dto.request.UpdateVisitResultRequest;
// import uz.snow.clinic.visit.model.dto.response.VisitResultResponse;
// import java.util.Map;

    // PATCH /api/v1/visits/results/{id}
// Doctor updates a single analysis result
    @PatchMapping("/results/{id}")
    public ResponseEntity<ApiResponse<VisitResultResponse>> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVisitResultRequest request) {

        request.setId(id);
        VisitResultResponse updated = visitService.updateResult(request);

        return ResponseEntity.ok(
                ApiResponse.success("Result updated successfully", updated));
    }

    // PATCH /api/v1/visits/{id}/diagnosis
// Doctor updates their diagnosis/notes for a visit
    @PatchMapping("/{id}/diagnosis")
    public ResponseEntity<ApiResponse<VisitResponse>> updateDiagnosis(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String diagnosis = body.get("diagnosis");
        VisitResponse updated = visitService.updateDiagnosis(id, diagnosis);

        return ResponseEntity.ok(
                ApiResponse.success("Diagnosis updated successfully", updated));
    }
}