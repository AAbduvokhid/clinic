package uz.snow.clinic.visit.model.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;


@Data
public class RegisterVisitResultRequest {
    @NotNull(message = "Analysis ID is required")
    private Long analysisId;

    private String resultValue;
    private String notes;

    // Price can be overridden at visit time
    private BigDecimal price;

}
