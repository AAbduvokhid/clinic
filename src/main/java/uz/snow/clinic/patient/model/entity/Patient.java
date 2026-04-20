package uz.snow.clinic.patient.model.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.patient.model.enums.Gender;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "patients")
public class Patient extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    @Column(name="address")
    private String address;
    @Column(name="notes")
    private String notes;
    // Link to User account — null if patient was registered by staff (no account)
    // Not null if patient self-registered via the portal
    @Column(name = "user_id", unique = true)
    private Long userId;
}
