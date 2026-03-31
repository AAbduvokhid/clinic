package uz.snow.clinic.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.doctor.model.entity.Doctor;
import uz.snow.clinic.doctor.model.enums.DoctorStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Check duplicate phone
    boolean existsByPhone(String phone);

    // Find by phone
    Optional<Doctor> findByPhone(String phone);

    // Find all doctors in a department
    List<Doctor> findAllByDepartmentId(Long departmentId);

    // Find all active doctors
    List<Doctor> findAllByStatus(DoctorStatus status);

    // Find active doctors in a department
    List<Doctor> findAllByDepartmentIdAndStatus(Long departmentId, DoctorStatus status);
}
