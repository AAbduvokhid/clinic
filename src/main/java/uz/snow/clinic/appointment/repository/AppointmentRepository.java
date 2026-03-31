package uz.snow.clinic.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.appointment.model.entity.Appointment;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByPatientId(Long patientId);
    // All appointments for a doctor
    List<Appointment> findAllByDoctorId(Long doctorId);
    // All appointments for a department
    List<Appointment> findAllByDepartmentId(Long departmentId);

    List<Appointment> findAllByStatus(AppointmentStatus status);
    List<Appointment> findAllByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
    // All appointments between two dates
    List<Appointment> findAllByAppointmentDateBetween(
            LocalDateTime start, LocalDateTime end);

}
