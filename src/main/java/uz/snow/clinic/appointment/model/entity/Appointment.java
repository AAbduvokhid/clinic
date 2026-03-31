package uz.snow.clinic.appointment.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    @Column(name = "registered_by", nullable = false)
    private Long registeredBy;
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;
    @Column(name = "price")
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private AppointmentStatus status;
    @Column(name = "notes")
    private String notes;
}
