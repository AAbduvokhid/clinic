package uz.snow.clinic.visit.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import uz.snow.clinic.user.model.entity.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visit_resuls")
public class VisitResult extends BaseEntity {

    @Column(name = "visit_id", nullable = false)
    private Long visitId;
    @Column(name = "analysis_id", nullable = false)
    private Long analysisId;
    @Column(name = "analysis_name", nullable = false)
    private String analysisName;
    // The actual result value e.g. "5.2 mmol/L"
    @Column(name = "result_value")
    private String resultValue;
    // Normal range e.g. "3.9 - 6.1"
    @Column(name = "norms")
    private String norms;
    @Column(name = "measurement")
    private String measurement;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "notes")
    private String notes;

}
