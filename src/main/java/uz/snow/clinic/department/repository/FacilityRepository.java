package uz.snow.clinic.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.department.model.entity.Facility;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // Find facility by name
    // Useful for duplicate name checking
    Optional<Facility> findByName(String name);

    // Check if facility with given name already exists
    // Replaces the manual stream filter in controller — DB does the work
    boolean existsByName(String name);
}

