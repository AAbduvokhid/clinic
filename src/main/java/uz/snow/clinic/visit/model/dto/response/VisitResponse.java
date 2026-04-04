package uz.snow.clinic.visit.model.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.visit.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VisitResponse {
    private Long id;
    private Long appointmentId;

    private Long patientId;
    private String patientFullName;

    private Long doctorId;
    private String doctorFullName;

    private Long departmentId;
    private String departmentName;

    private LocalDateTime visitDate;
    private String diagnosis;

    private List<VisitResultResponse> results;

    private BigDecimal totalPrice;
    private BigDecimal paidAmount;
    private PaymentStatus paymentStatus;

    private Long referrerId;
    private String referrerName;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
