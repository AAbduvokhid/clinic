package uz.snow.clinic.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uz.snow.clinic.visit.model.entity.Visit;
import uz.snow.clinic.visit.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Visit, Long> {

    // Count visits between dates
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.visitDate BETWEEN :start AND :end")
    Long countVisitsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Count unique patients between dates
    @Query("SELECT COUNT(DISTINCT v.patientId) FROM Visit v " +
            "WHERE v.visitDate BETWEEN :start AND :end")
    Long countUniquePatientsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Total income between dates
    @Query("SELECT COALESCE(SUM(v.totalPrice), 0) FROM Visit v " +
            "WHERE v.visitDate BETWEEN :start AND :end")
    BigDecimal sumIncomeBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Total paid amount between dates
    @Query("SELECT COALESCE(SUM(v.paidAmount), 0) FROM Visit v " +
            "WHERE v.visitDate BETWEEN :start AND :end")
    BigDecimal sumPaidAmountBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Count unpaid visits
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.paymentStatus = :status")
    Long countByPaymentStatus(@Param("status") PaymentStatus status);

    // Sum unpaid amount
    @Query("SELECT COALESCE(SUM(v.totalPrice - v.paidAmount), 0) FROM Visit v " +
            "WHERE v.paymentStatus = :status")
    BigDecimal sumUnpaidAmount(@Param("status") PaymentStatus status);

    // Top doctors by visit count
    @Query("SELECT v.doctorId, COUNT(v) as visitCount, " +
            "COALESCE(SUM(v.totalPrice), 0) as totalIncome " +
            "FROM Visit v GROUP BY v.doctorId " +
            "ORDER BY visitCount DESC")
    List<Object[]> findTopDoctors();

    // Monthly income for current year
    @Query("SELECT YEAR(v.visitDate), MONTH(v.visitDate), " +
            "COUNT(v), COALESCE(SUM(v.totalPrice), 0), " +
            "COALESCE(SUM(v.paidAmount), 0) " +
            "FROM Visit v WHERE YEAR(v.visitDate) = :year " +
            "GROUP BY YEAR(v.visitDate), MONTH(v.visitDate) " +
            "ORDER BY MONTH(v.visitDate)")
    List<Object[]> findMonthlyIncome(@Param("year") int year);
}