package uz.snow.clinic.department.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.department.model.dto.response.FacilityResponse;
import uz.snow.clinic.department.model.entity.Facility;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class FacilityMapper {
    public FacilityResponse toResponse(Facility facility) {
        return FacilityResponse.builder()
                .id(facility.getId())
                .name(facility.getName())
                .analysis(AnalysisMapper.toResponseList(
                        facility.getAnalyses().stream().toList()
                ))
                .createdAt(facility.getCreatedAt())
                .updatedAt(facility.getUpdatedAt())
                .build();
    }

    public List<FacilityResponse> toResponseList    (List<Facility> facilities) {
        return facilities.stream()
                .map(FacilityMapper::toResponse)
                .sorted(Comparator.comparingLong(FacilityResponse::getId))
                .toList();
    }
}
