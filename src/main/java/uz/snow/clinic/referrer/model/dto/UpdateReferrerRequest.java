package uz.snow.clinic.referrer.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class UpdateReferrerRequest  {
    // ID required — we need to know which referrer to update
    @NotNull(message = "Referrer ID is required")
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Unique code is required")
    @Size(min = 3, max = 20, message = "Unique code must be between 3 and 20 characters")
    private String uniqueCode;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Percentage must be greater than 0")
    @DecimalMax(value = "100.0", message = "Percentage cannot exceed 100")
    private BigDecimal percentage;

}
