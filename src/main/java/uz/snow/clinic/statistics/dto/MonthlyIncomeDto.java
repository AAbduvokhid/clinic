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
public class MonthlyIncomeDto {
    private int year;
    private int month;
    private String monthName;
    private Long visitCount;
    private BigDecimal totalIncome;
    private BigDecimal paidAmount;
}
