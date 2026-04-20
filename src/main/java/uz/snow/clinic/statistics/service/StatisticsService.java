package uz.snow.clinic.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.department.repository.DepartmentRepository;
import uz.snow.clinic.doctor.repository.DoctorRepository;
import uz.snow.clinic.patient.repository.PatientRepository;
import uz.snow.clinic.statistics.dto.DoctorStatDto;
import uz.snow.clinic.statistics.dto.MonthlyIncomeDto;
import uz.snow.clinic.statistics.dto.StatisticsResponse;
import uz.snow.clinic.statistics.repository.StatisticsRepository;
import uz.snow.clinic.visit.model.enums.PaymentStatus;
import uz.snow.clinic.visit.repository.VisitRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitRepository visitRepository;

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {

        // Today's date range
        LocalDateTime todayStart = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = LocalDateTime.now()
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Today stats
        Long todayVisits = statisticsRepository
                .countVisitsBetween(todayStart, todayEnd);
        Long todayPatients = statisticsRepository
                .countUniquePatientsBetween(todayStart, todayEnd);
        BigDecimal todayIncome = statisticsRepository
                .sumIncomeBetween(todayStart, todayEnd);
        BigDecimal todayPaidAmount = statisticsRepository
                .sumPaidAmountBetween(todayStart, todayEnd);

        // Overall stats
        Long totalPatients = patientRepository.count();
        Long totalDoctors = doctorRepository.count();
        Long totalVisits = visitRepository.count();
        BigDecimal totalIncome = statisticsRepository
                .sumIncomeBetween(LocalDateTime.of(2000, 1, 1, 0, 0),
                        LocalDateTime.now());

        // Unpaid stats
        Long unpaidVisitsCount = statisticsRepository
                .countByPaymentStatus(PaymentStatus.UNPAID);
        BigDecimal unpaidAmount = statisticsRepository
                .sumUnpaidAmount(PaymentStatus.UNPAID);

        // Top doctors
        List<DoctorStatDto> topDoctors = statisticsRepository
                .findTopDoctors()
                .stream()
                .limit(5)
                .map(row -> {
                    Long doctorId = (Long) row[0];
                    Long visitCount = (Long) row[1];
                    BigDecimal income = (BigDecimal) row[2];

                    String doctorName = doctorRepository.findById(doctorId)
                            .map(d -> d.getFirstName() + " " + d.getLastName())
                            .orElse("Unknown");

                    String departmentName = doctorRepository.findById(doctorId)
                            .flatMap(d -> departmentRepository.findById(d.getDepartmentId()))
                            .map(dep -> dep.getName())
                            .orElse("Unknown");

                    return DoctorStatDto.builder()
                            .doctorId(doctorId)
                            .doctorFullName(doctorName)
                            .departmentName(departmentName)
                            .visitCount(visitCount)
                            .totalIncome(income)
                            .build();
                })
                .toList();

        // Monthly income for current year
        int currentYear = LocalDateTime.now().getYear();
        List<MonthlyIncomeDto> monthlyIncome = statisticsRepository
                .findMonthlyIncome(currentYear)
                .stream()
                .map(row -> {
                    int year = ((Number) row[0]).intValue();
                    int month = ((Number) row[1]).intValue();
                    Long visitCount = ((Number) row[2]).longValue();
                    BigDecimal income = (BigDecimal) row[3];
                    BigDecimal paid = (BigDecimal) row[4];

                    String monthName = Month.of(month)
                            .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                    return MonthlyIncomeDto.builder()
                            .year(year)
                            .month(month)
                            .monthName(monthName)
                            .visitCount(visitCount)
                            .totalIncome(income)
                            .paidAmount(paid)
                            .build();
                })
                .toList();

        return StatisticsResponse.builder()
                .todayVisits(todayVisits)
                .todayPatients(todayPatients)
                .todayIncome(todayIncome)
                .todayPaidAmount(todayPaidAmount)
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalVisits(totalVisits)
                .totalIncome(totalIncome)
                .unpaidVisitsCount(unpaidVisitsCount)
                .unpaidAmount(unpaidAmount)
                .topDoctors(topDoctors)
                .monthlyIncome(monthlyIncome)
                .build();
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getStatisticsByDateRange(
            LocalDateTime start, LocalDateTime end) {

        Long visits = statisticsRepository.countVisitsBetween(start, end);
        Long patients = statisticsRepository.countUniquePatientsBetween(start, end);
        BigDecimal income = statisticsRepository.sumIncomeBetween(start, end);
        BigDecimal paid = statisticsRepository.sumPaidAmountBetween(start, end);

        return StatisticsResponse.builder()
                .todayVisits(visits)
                .todayPatients(patients)
                .todayIncome(income)
                .todayPaidAmount(paid)
                .build();
    }
}