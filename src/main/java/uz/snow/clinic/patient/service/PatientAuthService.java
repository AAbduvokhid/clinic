package uz.snow.clinic.patient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.snow.clinic.auth.service.JwtService;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.BaseException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.patient.model.dto.request.PatientLoginRequest;
import uz.snow.clinic.patient.model.dto.request.PatientRegisterRequest;
import uz.snow.clinic.patient.model.dto.response.PatientAuthResponse;
import uz.snow.clinic.patient.model.entity.Patient;
import uz.snow.clinic.patient.repository.PatientRepository;
import uz.snow.clinic.user.model.entity.Role;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.model.enums.UserStatus;
import uz.snow.clinic.user.repository.RoleRepository;
import uz.snow.clinic.user.repository.UserRepository;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientAuthService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public PatientAuthResponse register(PatientRegisterRequest request) {

        // Check phone not already used by another patient
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException(
                    "A patient with phone " + request.getPhone() + " already exists. Please login instead.");
        }

        // Check phone not already used as a username
        if (userRepository.existsByUsername(request.getPhone())) {
            throw new AlreadyExistsException(
                    "An account with this phone already exists. Please login instead.");
        }

        // Get ROLE_USER
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Role ROLE_USER not found"));

        // Capitalize names
        String firstName = StringUtils.capitalize(request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(request.getLastName().trim().toLowerCase());

        // Create User account — phone is the username
        User user = User.builder()
                .username(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(firstName)
                .lastName(lastName)
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .roles(Collections.singleton(userRole))
                .build();

        User savedUser = userRepository.save(user);

        // Create Patient record linked to this user
        Patient patient = Patient.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(request.getPhone().trim())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .userId(savedUser.getId())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        // Generate token
        String token = jwtService.generateToken(savedUser.getUsername());

        log.info("Patient '{}  {}' self-registered successfully", firstName, lastName);

        return PatientAuthResponse.builder()
                .token(token)
                .patientId(savedPatient.getId())
                .firstName(savedPatient.getFirstName())
                .lastName(savedPatient.getLastName())
                .phone(savedPatient.getPhone())
                .gender(savedPatient.getGender())
                .dateOfBirth(savedPatient.getDateOfBirth())
                .address(savedPatient.getAddress())
                .build();
    }

    @Transactional
    public PatientAuthResponse login(PatientLoginRequest request) {

        // Find user by phone (phone is used as username for patients)
        User user = userRepository.findByUsername(request.getPhone())
                .orElseThrow(() -> new BaseException("Invalid phone number or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException("Invalid phone number or password");
        }

        // Check account is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BaseException("Your account is disabled. Please contact the clinic.");
        }

        // Find linked patient record
        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BaseException(
                        "No patient record linked to this account. Please contact the clinic."));

        // Generate token
        String token = jwtService.generateToken(user.getUsername());

        log.info("Patient '{}  {}' logged in successfully", patient.getFirstName(), patient.getLastName());

        return PatientAuthResponse.builder()
                .token(token)
                .patientId(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phone(patient.getPhone())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .build();
    }
}