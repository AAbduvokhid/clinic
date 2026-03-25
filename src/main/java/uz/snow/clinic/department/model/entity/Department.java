package uz.snow.clinic.department.model.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "departments")
public class Department extends BaseEntity {


    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "department_facilities",
            joinColumns = @JoinColumn(name = "department_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id", referencedColumnName = "id"))
   @ Builder.Default
    private Set<Facility> facilities = new HashSet<>();
}
