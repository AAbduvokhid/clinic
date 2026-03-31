package uz.snow.clinic.doctor.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import uz.snow.clinic.doctor.model.enums.DoctorStatus;

import java.math.BigDecimal;

@Data
public class UpdateDoctorRequest {
    @NotNull(message = "Doctor ID is required")
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Department is required")
    private Long departmentId;

    private String roomNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "Percentage must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Percentage cannot exceed 100")
    private BigDecimal percentage;

    @NotNull(message = "Status is required")
    private DoctorStatus status;
}
