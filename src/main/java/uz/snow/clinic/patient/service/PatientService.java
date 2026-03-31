package uz.snow.clinic.patient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.patient.mapper.PatientMapper;
import uz.snow.clinic.patient.model.dto.request.RegisterPatientRequest;
import uz.snow.clinic.patient.model.dto.request.UpdatePatientRequest;
import uz.snow.clinic.patient.model.dto.response.PatientResponse;
import uz.snow.clinic.patient.model.entity.Patient;
import uz.snow.clinic.patient.repository.PatientRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return PatientMapper.toResponses(patientRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Patient", id));
        return PatientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponse findByPhone(String phone) {
        Patient patient = patientRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(
                        "Patient not found with phone: " + phone));
        return PatientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> searchByName(String name) {
        return PatientMapper.toResponses(patientRepository.searchByName(name));
    }

    @Transactional
    public PatientResponse register(RegisterPatientRequest request) {
        //Chack dublicate phone
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Patient with phone: " + request.getPhone() + " allready exists ");
        }
        String firstName = StringUtils.capitalize(
                request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(
                request.getLastName().trim().toLowerCase());

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone().trim())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .notes(request.getNotes())
                .build();
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient '{}  {}' registered successfully",
                savedPatient.getFirstName(), savedPatient.getLastName());
        return PatientMapper.toResponse(savedPatient);

    }

    @Transactional
    public PatientResponse update(UpdatePatientRequest request) {

        Patient exists = patientRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Patient", request.getId()));

        if (!exists.getPhone().equals(request.getPhone())
                && patientRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Patient with phone: " + request.getPhone() + " allready exists ");
        }

        String firstName = StringUtils.capitalize(
                request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(
                request.getLastName().trim().toLowerCase());

        exists.setFirstName(request.getFirstName());
        exists.setLastName(request.getLastName());
        exists.setPhone(request.getPhone());
        exists.setDateOfBirth(request.getDateOfBirth());
        exists.setGender(request.getGender());
        exists.setAddress(request.getAddress());
        exists.setNotes(request.getNotes());

        Patient savedPatient = patientRepository.save(exists);
        log.info("Patient '{}  {}' updated successfully",
                savedPatient.getFirstName(), savedPatient.getLastName());
        return PatientMapper.toResponse(savedPatient);
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Patient", id));
        patientRepository.delete(patient);
        log.info("Patient with id '{}' deleted successfully", id);
    }
}
