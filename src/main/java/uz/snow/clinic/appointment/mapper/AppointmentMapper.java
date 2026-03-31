package uz.snow.clinic.appointment.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.appointment.model.dto.response.AppointmentResponse;
import uz.snow.clinic.appointment.model.entity.Appointment;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class AppointmentMapper {
    public AppointmentResponse toResponse(
            Appointment appointment,
            String patientFullName,
            String doctorFullName,
            String depatmentName
    ) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .patientFullName(patientFullName)
                .doctorId(appointment.getDoctorId())
                .doctorFullName(doctorFullName)
                .departmentId(appointment.getDepartmentId())
                .departmentName(depatmentName)
                .appointmentDate(appointment.getAppointmentDate())
                .price(appointment.getPrice())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

    public List<AppointmentResponse> toResponseList(
            List<Appointment> appointments,
            Function<Long, String> patientNameResolver,
            Function<Long, String> doctorNameResolver,
            Function<Long, String> departmentNameResolver) {
        return appointments.stream()
                .map(a -> toResponse(a,
                        patientNameResolver.apply(a.getPatientId()),
                        doctorNameResolver.apply(a.getDoctorId()),
                        departmentNameResolver.apply(a.getDepartmentId())))
                .sorted(Comparator.comparingLong(AppointmentResponse::getId))
                .toList();


    }
}
