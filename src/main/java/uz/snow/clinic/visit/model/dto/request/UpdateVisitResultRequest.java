package uz.snow.clinic.visit.model.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateVisitResultRequest {

    @NotNull(message = "Result ID is required")
    private Long id;

    // The actual test result value e.g. "5.2 mmol/L"
    private String resultValue;

    // Doctor's notes for this specific test
    private String notes;
}