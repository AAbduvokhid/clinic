package uz.snow.clinic.department.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.department.model.entity.Department;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private List<FacilityResponse> facilities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
