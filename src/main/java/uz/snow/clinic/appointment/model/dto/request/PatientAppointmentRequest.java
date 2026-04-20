package uz.snow.clinic.appointment.model.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientAppointmentRequest {

    // patientId comes from the token — not from request body
    // This prevents patients from booking on behalf of others

    @NotNull(message = "Doctor is required")
    private Long doctorId;

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    private String notes;
}