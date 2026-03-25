package uz.snow.clinic.department.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class FacilityResponse {
    private Long id;
    private String name;
    private List<AnalysisResponse> analysis;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
