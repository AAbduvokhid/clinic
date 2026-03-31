package uz.snow.clinic.doctor.model.dto.response;

import lombok.Builder;
import lombok.Data;
import uz.snow.clinic.doctor.model.enums.DoctorStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private Long departmentId;
    private String departmentName;
    private String roomNumber;
    private BigDecimal percentage;
    private DoctorStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}