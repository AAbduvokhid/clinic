package uz.snow.clinic.referrer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.referrer.model.entity.Referrer;

import java.util.Optional;

@Repository
public interface ReferrerRepository extends JpaRepository<Referrer, Long> {
    Optional<Referrer> findByUniqueCode(String uniqueCode);

    boolean existsByUniqueCode(String uniqueCode);

    // Check if phone already registered — useful validation
    boolean existsByPhone(String phone);

    // Find by phone — useful for lookup
    Optional<Referrer> findByPhone(String phone);
}
