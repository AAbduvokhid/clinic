package uz.snow.clinic.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorStatDto {
    private Long doctorId;
    private String doctorFullName;
    private String departmentName;
    private Long visitCount;
    private BigDecimal totalIncome;
}
