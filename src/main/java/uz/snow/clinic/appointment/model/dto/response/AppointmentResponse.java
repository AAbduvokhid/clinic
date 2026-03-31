package uz.snow.clinic.appointment.model.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponse {
    private Long id;

    private Long patientId;
    private String patientFullName;

    private Long doctorId;
    private String doctorFullName;

    private Long departmentId;
    private String departmentName;

    private LocalDateTime appointmentDate;
    private BigDecimal price;
    private AppointmentStatus status;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}