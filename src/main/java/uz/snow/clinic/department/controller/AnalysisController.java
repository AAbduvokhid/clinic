package uz.snow.clinic.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.department.model.dto.request.RegisterAnalysisRequest;
import uz.snow.clinic.department.model.dto.request.UpdateAnalysisRequest;
import uz.snow.clinic.department.model.dto.response.AnalysisResponse;
import uz.snow.clinic.department.model.dto.response.DepartmentResponse;
import uz.snow.clinic.department.model.entity.Analysis;
import uz.snow.clinic.department.service.AnalysisService;
import uz.snow.clinic.department.service.DepartmentService;
import uz.snow.clinic.department.service.FacilityService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import javax.net.ssl.HttpsURLConnection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analyses")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnalysisResponse>> register(
            @Valid @RequestBody RegisterAnalysisRequest request) {

        AnalysisResponse saved = analysisService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Analysis registered successfully", saved));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AnalysisResponse>> update(
            @Valid @RequestBody UpdateAnalysisRequest request) {

        AnalysisResponse updated = analysisService.update(request);

        return ResponseEntity.ok(ApiResponse.success("Analysis updated successfully", updated));
    }

    // DELETE /api/v1/analyses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        analysisService.delete(id);

        // 204 No Content — correct HTTP status for successful delete
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/analyses/department/{departmentId}
    // Useful endpoint — get all analyses for a specific department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<AnalysisResponse>>> findAllByDepartment(@PathVariable Long departmentId) {

        List<AnalysisResponse> analyses = analysisService.findAllByDepartmentId(departmentId);

        return ResponseEntity.ok(ApiResponse.success(" Analyses fetched successfully ", analyses));
    }

    // GET /api/v1/analyses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnalysisResponse>> findById(@PathVariable Long id) {
        AnalysisResponse analysis = analysisService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Analysis fetched successfully ", analysis));
    }
}
