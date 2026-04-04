package uz.snow.clinic.visit.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.visit.model.dto.response.VisitResponse;
import uz.snow.clinic.visit.model.dto.response.VisitResultResponse;
import uz.snow.clinic.visit.model.entity.Visit;
import uz.snow.clinic.visit.model.entity.VisitResult;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class VisitMapper {
    public VisitResultResponse toResultResponse(VisitResult visitResult) {
        return VisitResultResponse.builder()
                .id(visitResult.getId())
                .analysisId(visitResult.getAnalysisId())
                .analysisName(visitResult.getAnalysisName())
                .resultValue(visitResult.getResultValue())
                .norms(visitResult.getNorms())
                .measurement(visitResult.getMeasurement())
                .price(visitResult.getPrice())
                .norms(visitResult.getNorms())
                .build();
    }

    public VisitResponse toResponse(
            Visit visit,
            String patientFullName,
            String doctorFullName,
            String departmentName,
            String referrerName,
            List<VisitResult> results) {
        return VisitResponse.builder()
                .id(visit.getId())
                .appointmentId(visit.getAppointmentId())
                .patientId(visit.getPatientId())
                .patientFullName(patientFullName)
                .doctorId(visit.getDoctorId())
                .doctorFullName(doctorFullName)
                .departmentId(visit.getDepartmentId())
                .departmentName(departmentName)
                .visitDate(visit.getVisitDate())
                .diagnosis(visit.getDiagnosis())
                .results(results.stream()
                        .map(VisitMapper::toResultResponse)
                        .toList())
                .totalPrice(visit.getTotalPrice())
                .paidAmount(visit.getPaidAmount())
                .paymentStatus(visit.getPaymentStatus())
                .referrerId(visit.getReferrerId())
                .referrerName(referrerName)
                .notes(visit.getNotes())
                .createdAt(visit.getCreatedAt())
                .updatedAt(visit.getUpdatedAt())
                .build();
    }

    public List<VisitResponse> toResponseList(
            List<Visit> visits,
            Function<Long, String> patientNameResolver,
            Function<Long, String> doctorNameResolver,
            Function<Long, String> departmentNameResolver,
            Function<Long, String> referrerNameResolver,
            Function<Long, List<VisitResult>> resultsResolver) {
        return visits.stream()
                .map(v -> toResponse(
                        v,
                        patientNameResolver.apply(v.getPatientId()),
                        doctorNameResolver.apply(v.getDoctorId()),
                        departmentNameResolver.apply(v.getDepartmentId()),
                        v.getReferrerId() != null
                                ? referrerNameResolver.apply(v.getReferrerId())
                                : null,
                        resultsResolver.apply(v.getId())))
                .sorted(Comparator.comparingLong(VisitResponse::getId).reversed())
                .toList();

    }
}
