package uz.snow.clinic.patient.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.patient.model.enums.Gender;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class PatientAuthResponse {

    private String token;


    private Long patientId;
    private String firstName;
    private String lastName;
    private String phone;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
}