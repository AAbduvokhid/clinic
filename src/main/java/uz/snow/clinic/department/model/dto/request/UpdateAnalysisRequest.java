package uz.snow.clinic.department.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class UpdateAnalysisRequest {
    @NotNull(message = "Analysis ID is required")
    private Long id;
    @NotBlank(message = "Analysis  name is required")
    private String name;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    @NotNull(message = "Department ID is required")
    private Long departmentId;
    @NotNull(message = "Facility ID is required")
    private Long facilityId;
    String norms;
    String measurement;
}
