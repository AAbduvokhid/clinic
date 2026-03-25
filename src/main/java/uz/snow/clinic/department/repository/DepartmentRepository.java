package uz.snow.clinic.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.department.model.entity.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    // Find department by its name
    // Used for validation — checking if department with same name already exists
    Optional<Department> findByName(String name);

    // Check if department with given name exists
    // More efficient than findByName() when you only need yes/no answer
    boolean existsByName(String name);
}
