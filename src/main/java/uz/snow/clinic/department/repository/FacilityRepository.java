package uz.snow.clinic.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.department.model.entity.Facility;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // Add to FacilityRepository:
    @Query("SELECT f FROM Facility f JOIN f.analyses a WHERE a.id = :analysisId")
    List<Facility> findAllByAnalysisId(@Param("analysisId") Long analysisId);
    // Find facility by name
    // Useful for duplicate name checking
    Optional<Facility> findByName(String name);

    // Check if facility with given name already exists
    // Replaces the manual stream filter in controller — DB does the work
    boolean existsByName(String name);
}

