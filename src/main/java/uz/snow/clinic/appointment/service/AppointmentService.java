package uz.snow.clinic.appointment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import uz.snow.clinic.appointment.mapper.AppointmentMapper;
import uz.snow.clinic.appointment.model.dto.request.RegisterAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.request.UpdateAppointmentRequest;
import uz.snow.clinic.appointment.model.dto.response.AppointmentResponse;
import uz.snow.clinic.appointment.model.entity.Appointment;
import uz.snow.clinic.appointment.model.enums.AppointmentStatus;
import uz.snow.clinic.appointment.repository.AppointmentRepository;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.repository.DepartmentRepository;
import uz.snow.clinic.doctor.repository.DoctorRepository;
import uz.snow.clinic.patient.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return AppointmentMapper.toResponseList(
                appointmentRepository.findAll(),
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName
        );
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Appointment", id));
        return toResponse(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAllByPatient(Long patientId) {
        return AppointmentMapper.toResponseList(
                appointmentRepository.findAllByPatientId(patientId),
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName
        );
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAllByDoctor(Long doctorId) {
        return AppointmentMapper.toResponseList(
                appointmentRepository.findAllByDoctorId(doctorId),
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAllByStatus(AppointmentStatus status) {
        return AppointmentMapper.toResponseList(
                appointmentRepository.findAllByStatus(status),
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName
        );
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAllByDataRange(LocalDateTime start, LocalDateTime end) {
        return AppointmentMapper.toResponseList(
                appointmentRepository.findAllByAppointmentDateBetween(start, end),
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName
        );
    }

    @Transactional

    public AppointmentResponse register(RegisterAppointmentRequest request, Long registeredBy) {
        //Validate patient exists
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> NotFoundException.of("Patient", request.getPatientId()));
        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> NotFoundException.of("Doctor", request.getDoctorId()));
        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .departmentId(request.getDepartmentId())
                .registeredBy(registeredBy)
                .appointmentDate(request.getAppointmentDate())
                .price(request.getPrice())
                .status(AppointmentStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();
        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment registered successfully for patient id: {}",
                saved.getPatientId());
        return toResponse(saved);


    }
    @Transactional
    public AppointmentResponse update( UpdateAppointmentRequest request) {
        Appointment existing=appointmentRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Appointment", request.getId()));
        // Validate patient exists
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> NotFoundException.of("Patient", request.getPatientId()));

        // Validate doctor exists
        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> NotFoundException.of("Doctor", request.getDoctorId()));

        // Validate department exists
        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));
        existing.setPatientId(request.getPatientId());
        existing.setDoctorId(request.getDoctorId());
        existing.setDepartmentId(request.getDepartmentId());
        existing.setAppointmentDate(request.getAppointmentDate());
        existing.setPrice(request.getPrice());
        existing.setStatus(request.getStatus());
        existing.setNotes(request.getNotes());

        Appointment saved = appointmentRepository.save(existing);
        log.info("Appointment with id '{}' updated successfully", saved.getId());
        return toResponse(saved);
    }
    @Transactional
    public void cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Appointment", id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        log.info("Appointment with id '{}' cancelled successfully", id);
    }

    @Transactional
    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Appointment", id));
        appointmentRepository.delete(appointment);
        log.info("Appointment with id '{}' deleted successfully", id);
    }


    private AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentMapper.toResponse(
                appointment,
                getPatientFullName(appointment.getPatientId()),
                getDoctorFullName(appointment.getDoctorId()),
                getDepartmentName(appointment.getDepartmentId())
        );
    }

    private String getPatientFullName(Long patientId) {
        return patientRepository.findById(patientId)
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");
    }

    private String getDoctorFullName(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(doctor -> doctor.getFirstName() + " " + doctor.getLastName())
                .orElse("Unknown");
    }

    private String getDepartmentName(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(d -> d.getName())
                .orElse("Unknown");


    }
}


