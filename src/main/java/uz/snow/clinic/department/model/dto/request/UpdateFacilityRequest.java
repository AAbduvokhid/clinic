package uz.snow.clinic.department.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data

public class UpdateFacilityRequest {
    @NotNull(message = "Facility ID is required")
    private Long id;
    @NotBlank(message = "Facility name is required")
    private String name;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
