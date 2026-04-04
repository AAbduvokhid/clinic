package uz.snow.clinic.visit.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VisitResultResponse {
    private Long id;
    private Long analysisId;
    private String analysisName;
    private String resultValue;
    private String norms;
    private String measurement;
    private BigDecimal price;
    private String notes;
}
