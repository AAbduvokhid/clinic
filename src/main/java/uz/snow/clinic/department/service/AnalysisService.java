package uz.snow.clinic.department.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.mapper.AnalysisMapper;
import uz.snow.clinic.department.model.dto.request.RegisterAnalysisRequest;
import uz.snow.clinic.department.model.dto.request.UpdateAnalysisRequest;
import uz.snow.clinic.department.model.dto.response.AnalysisResponse;
import uz.snow.clinic.department.model.entity.Analysis;
import uz.snow.clinic.department.model.entity.Facility;
import uz.snow.clinic.department.repository.AnalysisRepository;
import uz.snow.clinic.department.repository.FacilityRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisRepository analysisRepository;
    private final FacilityRepository facilityRepository;

    @Transactional
    public AnalysisResponse register(RegisterAnalysisRequest request) {

        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> NotFoundException.of("Facility", request.getFacilityId()));

        // Check duplicate analysis name in this department — DB level check
        // Much more efficient than loading all analyses and filtering in memory
        if (analysisRepository.existsByNameAndDepartmentId(request.getName(), request.getDepartmentId())) {
            throw new AlreadyExistsException(
                    "Analysis with name ' " + request.getName() + " ' already exists in this department!"
            );
        }

        Analysis analysis = Analysis.builder()
                .name(request.getName())
                .price(request.getPrice())
                .departmentId(request.getDepartmentId())
                .norms(request.getNorms())
                .measurement(request.getMeasurement())
                .build();
        Analysis savedAnalysis = analysisRepository.save(analysis);

        // Link analysis to facility
        facility.getAnalyses().add(savedAnalysis);
        facilityRepository.save(facility);

        log.info("Analysis '{}' registered successfully", savedAnalysis.getName());
        return AnalysisMapper.toResponse(savedAnalysis);
    }

    @Transactional
    public AnalysisResponse update(UpdateAnalysisRequest request) {

        // Find existing analysis or throw — no Optional in controller
        Analysis existing = analysisRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Analysis", request.getId()));

        // Check facility exists
        facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> NotFoundException.of("Facility", request.getFacilityId()));

        // Only update allowed fields — id and departmentId never change
        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setNorms(request.getNorms());
        existing.setMeasurement(request.getMeasurement());

        Analysis savedAnalysis = analysisRepository.save(existing);

        log.info("Analysis '{}' updated successfully", savedAnalysis.getName());
        return AnalysisMapper.toResponse(savedAnalysis);
    }

    //Delete analysis by id
// In AnalysisService.delete():
    @Transactional
    public void delete(Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Analysis", id));

        List<Facility> facilities = facilityRepository.findAllByAnalysisId(id);
        for (Facility facility : facilities) {
            facility.getAnalyses().remove(analysis);
            facilityRepository.save(facility);
        }

        analysisRepository.deleteById(id);
        log.info("Analysis with id '{}' deleted successfully", id);
    }

    @Transactional(readOnly = true)
    public List<AnalysisResponse> findAllByDepartmentId(Long depatrmentId) {
        return AnalysisMapper.toResponseList(
                analysisRepository.findAllByDepartmentId(depatrmentId));
    }

    @Transactional(readOnly = true)
    public AnalysisResponse findById(Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Analysis", id));
        return AnalysisMapper.toResponse(analysis);
    }
}

