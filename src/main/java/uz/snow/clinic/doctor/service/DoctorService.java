package uz.snow.clinic.doctor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.repository.DepartmentRepository;
import uz.snow.clinic.doctor.mapper.DoctorMapper;
import uz.snow.clinic.doctor.model.dto.request.RegisterDoctorRequest;
import uz.snow.clinic.doctor.model.dto.request.UpdateDoctorRequest;
import uz.snow.clinic.doctor.model.dto.response.DoctorResponse;
import uz.snow.clinic.doctor.model.entity.Doctor;
import uz.snow.clinic.doctor.model.enums.DoctorStatus;
import uz.snow.clinic.doctor.repository.DoctorRepository;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAll() {
        return DoctorMapper.toResponses(
                doctorRepository.findAll(),
                this::getDepartmentName);
    }

    @Transactional(readOnly = true)
    public DoctorResponse findById(Long id) {
        Doctor doctror = doctorRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Doctor", id));
        return DoctorMapper.toResponse(doctror, getDepartmentName(doctror.getDepartmentId()));
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAllByDepartment(Long departmentId) {
        return DoctorMapper.toResponses(
                doctorRepository.findAllByDepartmentId(departmentId),
                this::getDepartmentName);
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAllActive() {
        return DoctorMapper.toResponses(
                doctorRepository.findAllByStatus(
                        DoctorStatus.ACTIVE), this::getDepartmentName);
    }

    @Transactional
    public DoctorResponse register(RegisterDoctorRequest request) {

        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));

        if (doctorRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Doctor with phone " + request.getPhone() + "already exists");
        }
        String firstName = StringUtils.capitalize(request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(request.getLastName().trim().toLowerCase());

        Doctor doctor = Doctor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(request.getPhone().trim())
                .departmentId(request.getDepartmentId())
                .roomNumber(request.getRoomNumber())
                .percentage(request.getPercentage())
                .status(DoctorStatus.ACTIVE)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor '{} {}' registered successfully",
                savedDoctor.getFirstName(), savedDoctor.getLastName());

        return DoctorMapper.toResponse(savedDoctor, getDepartmentName(savedDoctor.getDepartmentId()));
    }

    @Transactional
    public DoctorResponse update(UpdateDoctorRequest request) {

        Doctor exists = doctorRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Doctor", request.getId()));

        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));

        if (!exists.getDepartmentId().equals(request.getDepartmentId())
                && doctorRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Doctor with phone " + request.getPhone() + "already exists");
        }
        String firstName = StringUtils.capitalize(request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(request.getLastName().trim().toLowerCase());

        exists.setFirstName(firstName);
        exists.setLastName(lastName);
        exists.setPhone(request.getPhone());
        exists.setDepartmentId(request.getDepartmentId());
        exists.setRoomNumber(request.getRoomNumber());
        exists.setPercentage(request.getPercentage());
        exists.setStatus(request.getStatus());

        Doctor savedDoctor = doctorRepository.save(exists);
        log.info("Doctor '{} {} ' update successfully", savedDoctor.getFirstName(), savedDoctor.getLastName());

        return DoctorMapper.toResponse(savedDoctor, getDepartmentName(savedDoctor.getDepartmentId()));
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Doctor", id));
        doctorRepository.delete(doctor);
        log.info("Doctor with id '{}' deleted successfully", id);
    }



    private String getDepartmentName(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(d -> d.getName())
                .orElse("Unknown");
    }
}
