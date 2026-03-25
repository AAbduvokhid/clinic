package uz.snow.clinic.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.department.model.entity.Analysis;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    // Find all analyses belonging to a specific department
    // Used when displaying all analyses for a department
    List<Analysis> findAllByDepartmentId(Long departmentId);

    // Check if analysis with same name exists in a department
    // Replaces the manual stream filter in controller — much more efficient
    // Goes directly to DB instead of loading all analyses into memory
    boolean existsByNameAndDepartmentId(String name, Long departmentId);
}
