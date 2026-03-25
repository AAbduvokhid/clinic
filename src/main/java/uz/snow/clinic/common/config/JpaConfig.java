package uz.snow.clinic.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// This enables the JPA auditing feature globally in the application
// Required for @CreatedDate and @LastModifiedDate to work
@Configuration
@EnableJpaRepositories

public class JpaConfig {
}
