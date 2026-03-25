package uz.snow.clinic.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByStatus(UserStatus status);

    List<User> findAllByDepartmentId(Long departmentId);
}
