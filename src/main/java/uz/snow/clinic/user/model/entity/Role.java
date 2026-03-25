package uz.snow.clinic.user.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.model.enums.UserStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    // @NaturalId means this is a business key (unique identifier from business perspective)
    // JPA uses it for optimized lookups and caching
    @NaturalId
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    private RoleName name;

}
