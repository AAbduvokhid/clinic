package uz.snow.clinic.doctor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.doctor.model.dto.request.RegisterDoctorRequest;
import uz.snow.clinic.doctor.model.dto.request.UpdateDoctorRequest;
import uz.snow.clinic.doctor.model.dto.response.DoctorResponse;
import uz.snow.clinic.doctor.service.DoctorService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> findAll() {
        List<DoctorResponse> doctors = doctorService.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Doctors fetched successfully", doctors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> findById(@PathVariable Long id) {
        DoctorResponse doctor = doctorService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor fetched successfully", doctor));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> findAllByDepartmentId(@PathVariable Long departmentId) {
        List<DoctorResponse> doctors = doctorService.findAllByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Doctor fetched successfully", doctors));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> findAllActive() {
        List<DoctorResponse> activeDoctors = doctorService.findAllActive();
        return ResponseEntity.ok(ApiResponse.success("Active doctor fetched successfully", activeDoctors));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> register(@Valid @RequestBody RegisterDoctorRequest request) {
        DoctorResponse doctor = doctorService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor registered successfully", doctor));
    }
    @PutMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> update(
            @Valid @RequestBody UpdateDoctorRequest request) {
        DoctorResponse updated = doctorService.update(request);
        return ResponseEntity.ok(
                ApiResponse.success("Doctor updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
