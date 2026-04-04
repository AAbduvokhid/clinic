package uz.snow.clinic.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.snow.clinic.auth.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Key fix: do NOT expose DaoAuthenticationProvider as a @Bean
    // Build it privately and inject it directly into AuthenticationManager
    // This prevents Spring Security from getting confused
    private DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager uses our provider directly
    // NOT exposed as @Bean — avoids conflict with Spring Security auto-config
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/facilities/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/facilities/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/facilities/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/analyses/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/analyses/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/analyses/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/patients/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/patients").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/patients").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/patients/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctors/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/doctors").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/doctors").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/doctors/**").hasAuthority("ROLE_ADMIN")
                        // Appointment endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/appointments").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/appointments").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/appointments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/appointments/**").hasAuthority("ROLE_ADMIN")
                        // Visit endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/visits/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/visits").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/visits/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/visits/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}