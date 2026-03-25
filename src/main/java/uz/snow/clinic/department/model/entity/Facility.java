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
@Table(name = "facilities")
public class Facility extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "facility_analysis"
            , joinColumns = @JoinColumn(name = "facility_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "analysis_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Analysis> analyses = new HashSet<>();
}
