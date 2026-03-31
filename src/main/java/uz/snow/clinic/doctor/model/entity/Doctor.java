package uz.snow.clinic.doctor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.doctor.model.enums.DoctorStatus;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "room_number")
    private String roomNumber;
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    @Column(name = "precentage")
    private BigDecimal percentage;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DoctorStatus status;
}
