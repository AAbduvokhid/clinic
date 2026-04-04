package uz.snow.clinic.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.visit.model.entity.Visit;
import uz.snow.clinic.visit.model.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByPatientId(Long patientId);
    List<Visit> findAllByDoctorId(Long doctorId);
    List<Visit> findAllByDepartmentId(Long departmentId);
    List<Visit> findAllByPaymentStatus(PaymentStatus paymentStatus);
    List<Visit> findAllByReferrerId(Long referrerId);
    List<Visit> findAllByVisitDateBetween(LocalDateTime start, LocalDateTime end);
}
