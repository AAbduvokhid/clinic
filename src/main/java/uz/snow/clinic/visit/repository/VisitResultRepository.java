package uz.snow.clinic.visit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.visit.model.entity.VisitResult;

import java.util.List;


@Repository
public interface VisitResultRepository extends JpaRepository<VisitResult, Long> {
    List<VisitResult> findAllByVisitId(Long visitId);
    void deleteAllByVisitId(Long visitId);
}
