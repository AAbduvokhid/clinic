package uz.snow.clinic.user.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import uz.snow.clinic.user.model.enums.RoleName;
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    // @NaturalId means this is a business key (unique identifier from business perspective)
    // JPA uses it for optimized lookups and caching
    @NaturalId
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName name;

}
