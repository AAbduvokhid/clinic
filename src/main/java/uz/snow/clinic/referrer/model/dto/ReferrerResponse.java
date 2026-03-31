package uz.snow.clinic.referrer.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
public class ReferrerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String uniqueCode;
    private String phone;
    private BigDecimal percentage;

    // Audit fields — useful for admin panels
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
