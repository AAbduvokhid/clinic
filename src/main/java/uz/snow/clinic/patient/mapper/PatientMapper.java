package uz.snow.clinic.patient.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.patient.model.dto.response.PatientResponse;
import uz.snow.clinic.patient.model.entity.Patient;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class PatientMapper {
    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .notes(patient.getNotes())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }

    public List<PatientResponse> toResponses(List<Patient> patients) {
        return patients.stream()
                .map(PatientMapper::toResponse)
                .sorted(Comparator.comparingLong(PatientResponse::getId).reversed())
                .toList();
    }
}
