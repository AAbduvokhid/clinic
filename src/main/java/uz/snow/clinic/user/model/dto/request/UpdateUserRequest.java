package uz.snow.clinic.user.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class UpdateUserRequest {

    @NotNull(message = "User ID is required")
    private Long id;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Department is required")
    private Long departmentId;

}
