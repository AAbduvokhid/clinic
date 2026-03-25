package uz.snow.clinic.referrer.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.referrer.model.dto.ReferrerResponse;
import uz.snow.clinic.referrer.model.entity.Referrer;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class ReferrerMapper {
    public ReferrerResponse toResponse(Referrer referrer) {
        return ReferrerResponse.builder()
                .id(referrer.getId())
                .firstName(referrer.getFirstName())
                .lastName(referrer.getLastName())
                .uniqueCode(referrer.getUniqueCode())
                .phone(referrer.getPhone())
                .percentage(referrer.getPercentage())
                .createdAt(referrer.getCreatedAt())
                .updatedAt(referrer.getUpdatedAt())
                .build();
    }

    public List<ReferrerResponse> toResponseList(List<Referrer> referrers) {
        return referrers.stream()
                .map(ReferrerMapper::toResponse)
                .sorted(Comparator.comparingLong(ReferrerResponse::getId))
                .toList();
    }
}
