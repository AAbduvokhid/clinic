package uz.snow.clinic.appointment.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateAppointmentRequest {

    @NotNull(message = "Appointment ID is required")
    private Long id;

    @NotNull(message = "Patient is required")
    private Long patientId;

    @NotNull(message = "Doctor is required")
    private Long doctorId;

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Appointment date is required")
    private LocalDateTime appointmentDate;

    private BigDecimal price;
    private String notes;

    @NotNull(message = "Status is required")
    private AppointmentStatus status;
}