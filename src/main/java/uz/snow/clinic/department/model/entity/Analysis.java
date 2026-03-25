package uz.snow.clinic.department.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "analysis")
public class Analysis extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "norms")
    private String norms;

    @Column(name = "measurement")
    private String measurement;

}
