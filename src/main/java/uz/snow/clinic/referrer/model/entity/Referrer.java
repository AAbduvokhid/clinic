package uz.snow.clinic.referrer.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import uz.snow.clinic.user.model.entity.BaseEntity;
import uz.snow.clinic.user.model.enums.UserStatus;

import java.math.BigDecimal;
import java.nio.Buffer;
import java.time.ZonedDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="referrers")
public class Referrer extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    @NaturalId(mutable = true)
    @Column(name = "unique_code", nullable = false, unique = true)
    private String uniqueCode;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;
}
