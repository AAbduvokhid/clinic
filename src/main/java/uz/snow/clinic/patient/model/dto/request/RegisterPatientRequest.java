package uz.snow.clinic.patient.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import uz.snow.clinic.patient.model.enums.Gender;

import java.time.LocalDate;

@Data
public class RegisterPatientRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$",message = "Invalid phone number format")
    private String phone;
    @Past(message = "Date of birth must be in the past ")
    private LocalDate dateOfBirth;
    @NotNull(message = "Gender is required")
    private Gender gender;
    private String address;
    private String notes;
}
