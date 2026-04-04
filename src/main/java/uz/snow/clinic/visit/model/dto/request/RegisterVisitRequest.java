
   package uz.snow.clinic.visit.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.snow.clinic.visit.model.enums.PaymentStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

    @Data
    public class RegisterVisitRequest {

        // Optional — if visit comes from an appointment
        private Long appointmentId;

        @NotNull(message = "Patient is required")
        private Long patientId;

        @NotNull(message = "Doctor is required")
        private Long doctorId;

        @NotNull(message = "Department is required")
        private Long departmentId;

        @NotNull(message = "Visit date is required")
        private LocalDateTime visitDate;

        // List of analyses performed in this visit
        @NotEmpty(message = "At least one analysis result is required")
        private List<RegisterVisitResultRequest> results;

        private String diagnosis;
        private BigDecimal paidAmount;
        private PaymentStatus paymentStatus;
        private Long referrerId;
        private String notes;
    }
