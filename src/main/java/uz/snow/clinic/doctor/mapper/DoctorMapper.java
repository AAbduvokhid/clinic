package uz.snow.clinic.doctor.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.doctor.model.dto.response.DoctorResponse;
import uz.snow.clinic.doctor.model.entity.Doctor;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class DoctorMapper {
    public DoctorResponse toResponse(Doctor doctor, String departmentName) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .phone(doctor.getPhone())
                .departmentId(doctor.getDepartmentId())
                .departmentName(departmentName)
                .roomNumber(doctor.getRoomNumber())
                .percentage(doctor.getPercentage())
                .status(doctor.getStatus())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    public List<DoctorResponse> toResponses(
            List<Doctor> doctors, java.util.function.Function<Long, String> departmentNameResolver) {
        return doctors.stream()
                .map(d -> toResponse(d, departmentNameResolver.apply(d.getDepartmentId())))
                .sorted(Comparator.comparingLong(DoctorResponse::getId).reversed())
                .toList();
    }
}
