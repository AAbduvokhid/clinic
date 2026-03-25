package uz.snow.clinic.department.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.snow.clinic.department.model.dto.response.DepartmentResponse;
import uz.snow.clinic.department.model.dto.response.FacilityResponse;
import uz.snow.clinic.department.service.DepartmentService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> findAll() {
        List<DepartmentResponse> departments = departmentService.findAll();
        return  ResponseEntity.ok(ApiResponse.success("Departments fetched successfully ", departments));
    }
    // GET /api/v1/departments/{id}
@GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> findById(@PathVariable Long id) {
        DepartmentResponse department = departmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Department fetched successfully ", department));
}
}
