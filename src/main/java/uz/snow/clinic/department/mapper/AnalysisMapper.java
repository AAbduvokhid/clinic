package uz.snow.clinic.department.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.department.model.dto.response.AnalysisResponse;
import uz.snow.clinic.department.model.entity.Analysis;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class AnalysisMapper {
    public AnalysisResponse toResponse(Analysis analysis) {
        return  AnalysisResponse.builder()
                .id(analysis.getId())
                .name(analysis.getName())
                .price(analysis.getPrice())
                .departmentId(analysis.getDepartmentId())
                .norms(analysis.getNorms())
                .measurement(analysis.getMeasurement())
                .createdAt(analysis.getCreatedAt())
                .updatedAt(analysis.getUpdatedAt())
                .build();
    }
    public List<AnalysisResponse> toResponseList(List<Analysis> analyses) {
        return analyses.stream()
                .map(AnalysisMapper::toResponse)
                .sorted(Comparator.comparingLong(AnalysisResponse::getId))
                .toList();
    }
}
