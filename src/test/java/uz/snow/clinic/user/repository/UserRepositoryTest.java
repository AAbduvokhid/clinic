/*
package uz.snow.clinic.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.repository.UserRepository;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // Rolls back the database after the test
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPopulateAuditFields() {
        // 1. Create a new user (don't manually set ID or timestamps)
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        // ... set other mandatory fields

        // 2. Save to database
        User savedUser = userRepository.save(user);

        // 3. Verify that Spring Data JPA filled in the blanks
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();

        System.out.println("Created At: " + savedUser.getCreatedAt());
    }
}*/
