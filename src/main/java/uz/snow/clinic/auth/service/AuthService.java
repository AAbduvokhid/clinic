package uz.snow.clinic.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.auth.dto.request.LoginRequest;
import uz.snow.clinic.auth.dto.response.LoginResponse;
import uz.snow.clinic.auth.service.JwtService;
import uz.snow.clinic.common.exception.BaseException;
import uz.snow.clinic.user.mapper.UserMapper;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BaseException("Invalid username or password"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new BaseException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        log.info("User '{}' logged in successfully", user.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .isAdmin(UserMapper.toResponse(user).isAdmin())
                .build();
    }
}