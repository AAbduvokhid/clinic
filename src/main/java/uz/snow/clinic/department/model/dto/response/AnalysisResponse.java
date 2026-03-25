package uz.snow.clinic.department.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class AnalysisResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long departmentId;
    private String norms;
    private String measurement; // Fixed typo: measurment -> measurement
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
