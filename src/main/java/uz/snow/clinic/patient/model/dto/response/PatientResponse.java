package uz.snow.clinic.patient.model.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.patient.model.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
