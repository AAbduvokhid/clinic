package uz.snow.clinic.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.department.model.dto.request.RegisterFacilityRequest;
import uz.snow.clinic.department.model.dto.request.UpdateFacilityRequest;
import uz.snow.clinic.department.model.dto.response.FacilityResponse;
import uz.snow.clinic.department.service.FacilityService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    public ResponseEntity<ApiResponse<FacilityResponse>> register(
            @Valid @RequestBody RegisterFacilityRequest request) {

        FacilityResponse saved = facilityService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Facility registered successfully ", saved));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<FacilityResponse>> update(
            @Valid @RequestBody UpdateFacilityRequest request) {
        FacilityResponse updated = facilityService.update(request);
        return ResponseEntity.ok(ApiResponse.success("Facility updated successfully ", updated));
    }

    // DELETE /api/v1/facilities/{id}
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/facilities/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacilityResponse>> findById(@PathVariable Long id) {
        FacilityResponse facility = facilityService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Facility fetched successfully ", facility));
    }

}
