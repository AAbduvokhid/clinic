package uz.snow.clinic.visit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.model.entity.Department;
import uz.snow.clinic.department.repository.AnalysisRepository;
import uz.snow.clinic.department.repository.DepartmentRepository;
import uz.snow.clinic.doctor.repository.DoctorRepository;
import uz.snow.clinic.patient.repository.PatientRepository;
import uz.snow.clinic.referrer.repository.ReferrerRepository;
import uz.snow.clinic.visit.mapper.VisitMapper;
import uz.snow.clinic.visit.model.dto.request.RegisterVisitRequest;
import uz.snow.clinic.visit.model.dto.request.RegisterVisitResultRequest;
import uz.snow.clinic.visit.model.dto.response.VisitResponse;
import uz.snow.clinic.visit.model.entity.Visit;
import uz.snow.clinic.visit.model.entity.VisitResult;
import uz.snow.clinic.visit.model.enums.PaymentStatus;
import uz.snow.clinic.visit.repository.VisitRepository;
import uz.snow.clinic.visit.repository.VisitResultRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitResultRepository visitResultRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final AnalysisRepository analysisRepository;
    private final ReferrerRepository referrerRepository;

    @Transactional(readOnly = true)
    public List<VisitResponse> findAll() {
        List<Visit> visits = visitRepository.findAll();
        return VisitMapper.toResponseList(
                visits,
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName,
                this::getReferrerName,
                this::getVisitResult);
    }

    @Transactional(readOnly = true)
    public VisitResponse findById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Visit", id));
        return toResponse(visit);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> findAllByPatient(Long id) {
        List<Visit> visits = visitRepository.findAllByPatientId(id);
        return VisitMapper.toResponseList(visits,
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName,
                this::getReferrerName,
                this::getVisitResult);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> findAllByDoctor(Long doctorId) {
        List<Visit> visits = visitRepository.findAllByDoctorId(doctorId);
        return VisitMapper.toResponseList(
                visits,
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName,
                this::getReferrerName,
                this::getVisitResult);
    }
    @Transactional(readOnly = true)
    public List<VisitResponse> findAllByPaymentStatus(PaymentStatus status) {
        List<Visit> visits = visitRepository.findAllByPaymentStatus(status);
        return VisitMapper.toResponseList(visits,
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName,
                this::getReferrerName,
                this::getVisitResult);
    }

    @Transactional(readOnly = true)
    public List<VisitResponse> findAllByDateRange(
            LocalDateTime start, LocalDateTime end) {
        List<Visit> visits = visitRepository.findAllByVisitDateBetween(start, end);
        return VisitMapper.toResponseList(
                visits,
                this::getPatientFullName,
                this::getDoctorFullName,
                this::getDepartmentName,
                this::getReferrerName,
                this::getVisitResult);
    }

    @Transactional
    public VisitResponse register(RegisterVisitRequest request, Long registeredBy) {
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> NotFoundException.of("Patient", request.getPatientId()));
        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> NotFoundException.of("Doctor", request.getDoctorId()));
        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> NotFoundException.of("Department", request.getDepartmentId()));
        if (request.getReferrerId() != null) {
            referrerRepository.findById(request.getReferrerId())
                    .orElseThrow(() -> NotFoundException.of("Referrer", request.getReferrerId()));
        }
        List<VisitResult> results = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (RegisterVisitResultRequest resultRequest : request.getResults()) {
            var analysis = analysisRepository.findById(resultRequest.getAnalysisId())
                    .orElseThrow(() -> NotFoundException.of("Analysis", resultRequest.getAnalysisId()));

            //  Use provided price or fall back to analysis default price
            BigDecimal price = resultRequest.getPrice() != null
                    ? resultRequest.getPrice()
                    : analysis.getPrice();
            totalPrice = totalPrice.add(price);

            results.add(VisitResult.builder()
                    .analysisId(analysis.getId())
                    .analysisName(analysis.getName())
                    .resultValue(resultRequest.getResultValue())
                    .norms(analysis.getNorms())
                    .measurement(analysis.getMeasurement())
                    .price(price)
                    .notes(resultRequest.getNotes())
                    .build());
        }
        PaymentStatus paymentStatus = request.getPaymentStatus() != null
                ? request.getPaymentStatus()
                : PaymentStatus.UNPAID;

        Visit visit = Visit.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .departmentId(request.getDepartmentId())
                .registeredBy(registeredBy)
                .visitDate(request.getVisitDate())
                .diagnosis(request.getDiagnosis())
                .totalPrice(totalPrice)
                .paidAmount(request.getPaidAmount() != null
                        ? request.getPaidAmount()
                        : BigDecimal.ZERO)
                .paymentStatus(paymentStatus)
                .referrerId(request.getReferrerId())
                .notes(request.getNotes())
                .build();
        Visit savedVisit=visitRepository.save(visit);

        for (VisitResult result : results) {
            result.setVisitId(savedVisit.getId());
        }
        visitResultRepository.saveAll(results);

        log.info("Visit registered successfully for patient id: {}",
                savedVisit.getPatientId());
        return toResponse(savedVisit);
    }

    @Transactional
    public VisitResponse updatePayment(Long id, BigDecimal paidAmount,
                                       PaymentStatus paymentStatus) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Visit", id));

        visit.setPaidAmount(paidAmount);
        visit.setPaymentStatus(paymentStatus);

        Visit saved = visitRepository.save(visit);
        log.info("Visit payment updated for visit id: {}", id);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Visit", id));

        // Delete results first
        visitResultRepository.deleteAllByVisitId(id);
        visitRepository.delete(visit);
        log.info("Visit with id '{}' deleted successfully", id);
    }

    //Helper methods
    private VisitResponse toResponse(Visit visit) {
        return VisitMapper.toResponse(
                visit,
                getPatientFullName(visit.getPatientId()),
                getDoctorFullName(visit.getDoctorId()),
                getDepartmentName(visit.getDepartmentId()),
                visit.getReferrerId() != null
                        ? getReferrerName(visit.getReferrerId())
                        : null,
                getVisitResult(visit.getId())
        );
    }

    private List<VisitResult> getVisitResult(Long visitId) {
        return visitResultRepository.findAllByVisitId(visitId);
    }

    private String getPatientFullName(Long patientId) {
        return patientRepository.findById(patientId)
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("unknown");
    }

    private String getDoctorFullName(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(d -> d.getLastName() + " " + d.getLastName())
                .orElse("unknown");
    }

    private String getDepartmentName(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(Department::getName)
                .orElse("unknown");
    }

    private String getReferrerName(Long referrerId) {
        return referrerRepository.findById(referrerId)
                .map(r -> r.getFirstName() + " " + r.getLastName())
                .orElse("unknown");
    }
}
