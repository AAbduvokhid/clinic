package uz.snow.clinic.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.patient.model.dto.request.RegisterPatientRequest;
import uz.snow.clinic.patient.model.dto.request.UpdatePatientRequest;
import uz.snow.clinic.patient.model.dto.response.PatientResponse;
import uz.snow.clinic.patient.service.PatientService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> findAll() {
        List<PatientResponse> patients = patientService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Patient fetched successfully", patients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> findById(@PathVariable Long id) {
        PatientResponse patient=patientService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Patient fetched successfully", patient));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ApiResponse<PatientResponse>> findByPhone(@PathVariable String phone) {
        PatientResponse patient=patientService.findByPhone(phone);
        return ResponseEntity.ok(ApiResponse.success("Patient fetched successfully", patient));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> searchByName(@RequestParam String name) {
        List<PatientResponse> patients = patientService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success("Patient fetched successfully", patients));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> register (@Valid @RequestBody RegisterPatientRequest request) {
        PatientResponse saved=patientService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient registered successfully", saved));

    }
    @PutMapping
    public ResponseEntity<ApiResponse<PatientResponse>> update(@Valid @RequestBody UpdatePatientRequest request) {
        PatientResponse updaeted =patientService.update(request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", updaeted));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
