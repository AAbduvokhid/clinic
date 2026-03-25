package uz.snow.clinic.department.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.mapper.DepartmentMapper;
import uz.snow.clinic.department.model.dto.response.DepartmentResponse;
import uz.snow.clinic.department.model.entity.Department;
import uz.snow.clinic.department.repository.DepartmentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    public List<DepartmentResponse> findAll() {
        return DepartmentMapper.toResponseList(departmentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Department", id));
        return DepartmentMapper.toResponse(department);
    }

    // Internal method — returns entity for use within service layer
    // Package-private — only services in same package can use it
    // This is used by FacilityService and AnalysisService to get Department entity
    @Transactional(readOnly = true)
    Department findEntityById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Department", id));
    }

    // Save department — used internally
    @Transactional
    public DepartmentResponse save(Department department) {
        Department saved = departmentRepository.save(department);
        return DepartmentMapper.toResponse(saved);
    }
}
