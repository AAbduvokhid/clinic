package uz.snow.clinic.visit.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.user.model.entity.BaseEntity;
import uz.snow.clinic.visit.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {
    @Column(name = "appointment_id")
    private Long appointmentId;
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    @Column(name = "depatment_id", nullable = false)
    private Long departmentId;
    @Column(name = "registered_by",nullable = false)
    private Long registeredBy;
    @Column(name = "visit_date ")
    private LocalDateTime visitDate;
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "paid_amount")
    private BigDecimal paidAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name = "referrer_id")
    private Long referrerId;
    @Column(name = "notes")
    private String notes;

}
