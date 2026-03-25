package uz.snow.clinic.department.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.department.model.dto.response.DepartmentResponse;
import uz.snow.clinic.department.model.entity.Department;

import java.util.Comparator;
import java.util.List;

@UtilityClass

public class DepartmentMapper {
    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .facilities(FacilityMapper.toResponseList(
                        department.getFacilities()
                                .stream()
                                .toList()))
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    public List<DepartmentResponse> toResponseList(List<Department> departments) {
        return departments.stream()
                .map(DepartmentMapper::toResponse)
                .sorted(Comparator.comparingLong(DepartmentResponse::getId))
                .toList();
    }

}
