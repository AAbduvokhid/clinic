package uz.snow.clinic.user.model.dto.request;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    // @NotBlank checks that string is not null AND not empty AND not just spaces
    // Better than @NotNull for String fields
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required ")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "First name is required ")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    // @Pattern validates phone format — adjust regex to your country format
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Department is required  ")
    private Long departmentId;


}
