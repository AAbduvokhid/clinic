package uz.snow.clinic.statistics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StatisticsResponse {
    // Today's stats
    private Long todayVisits;
    private Long todayPatients;
    private BigDecimal todayIncome;
    private BigDecimal todayPaidAmount;

    // Overall stats
    private Long totalPatients;
    private Long totalDoctors;
    private Long totalVisits;
    private BigDecimal totalIncome;

    // Unpaid info
    private Long unpaidVisitsCount;
    private BigDecimal unpaidAmount;

    // Top doctors
    private List<DoctorStatDto> topDoctors;

    // Monthly income
    private List<MonthlyIncomeDto> monthlyIncome;
}

