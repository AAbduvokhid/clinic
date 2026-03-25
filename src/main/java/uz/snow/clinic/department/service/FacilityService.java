package uz.snow.clinic.department.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.mapper.FacilityMapper;
import uz.snow.clinic.department.model.dto.request.RegisterFacilityRequest;
import uz.snow.clinic.department.model.dto.request.UpdateFacilityRequest;
import uz.snow.clinic.department.model.dto.response.FacilityResponse;
import uz.snow.clinic.department.model.entity.Department;
import uz.snow.clinic.department.model.entity.Facility;
import uz.snow.clinic.department.repository.DepartmentRepository;
import uz.snow.clinic.department.repository.FacilityRepository;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final DepartmentRepository departmentRepository;

    //Register new facility
    @Transactional
    public FacilityResponse register(RegisterFacilityRequest request) {
        // Check department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));

        // Check duplicate facility name — DB level, efficient
        if (facilityRepository.existsByName(request.getName())) {
            throw new AlreadyExistsException(
                    "Facility with name  " + request.getName() + " already exists"
            );
        }

        Facility facility = Facility.builder()
                .name(request.getName())
                .analyses(new HashSet<>()) // Empty set — no analyses yet
                .build();

        Facility savedFacility = facilityRepository.save(facility);

        // Link facility to department
        department.getFacilities().add(savedFacility);
        departmentRepository.save(department);

        log.info("Facility '{}' registered successfully", savedFacility.getName());
        return FacilityMapper.toResponse(savedFacility);
    }

    //Update existing  facility
    @Transactional
    public FacilityResponse update(UpdateFacilityRequest request) {

        // Find existing facility — throws if not found
        Facility existing = facilityRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Facility", request.getId()));

        // Check duplicate name — only if name is actually changing
        if (!existing.getName().equals(request.getName()) && facilityRepository.existsByName(request.getName())) {
            throw new AlreadyExistsException("Facility with name  " + request.getName() + " already exists");
        }

        // Only update name — analyses are NEVER wiped on update!
        existing.setName(request.getName());

        Facility savedFacility = facilityRepository.save(existing);
        log.info("Facility '{}' updated successfully", savedFacility.getName());
        return FacilityMapper.toResponse(savedFacility);
    }

    // Delete facility by id
    @Transactional
    public void delete(Long id) {
        if (!facilityRepository.existsById(id)) {
            throw NotFoundException.of("Facility", id);
        }
        facilityRepository.deleteById(id);
        log.info("Facility with id '{}' deleted successfully", id);
    }

    // Get single facility by id
    @Transactional(readOnly = true)
    public FacilityResponse findById(Long id) {

        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Facility", id));

        return FacilityMapper.toResponse(facility);
    }
}
