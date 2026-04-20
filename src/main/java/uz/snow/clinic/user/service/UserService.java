package uz.snow.clinic.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.enums.DepartmentName;
import uz.snow.clinic.user.mapper.UserMapper;
import uz.snow.clinic.user.model.dto.request.ResetPasswordRequest;
import uz.snow.clinic.user.model.dto.request.UpdateUserRequest;
import uz.snow.clinic.user.model.dto.request.UserRegistrationRequest;
import uz.snow.clinic.user.model.dto.response.UserResponse;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.model.enums.UserStatus;
import uz.snow.clinic.user.repository.UserRepository;

import java.rmi.AlreadyBoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    // add this method
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("User", request.getId()));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password reset for user: {}", user.getUsername());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return UserMapper.toUserResposnseList(users).stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .toList();
    }

    // Returns DTO directly — resolves Optional internally
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));
        return UserMapper.toResponse(user);
    }

    // Returns DTO directly — resolves Optional internally
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return UserMapper.toResponse(user);
    }

    @Transactional
    public UserResponse register(UserRegistrationRequest request, String departmentName) {
        // Check duplicate username — throws exception instead of returning boolean
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User already exists: " + request.getUsername());
        }

        // Role assignment logic based on department name
        RoleName roleName = DepartmentName.REGISTRATION.getDisplayName().equals(departmentName)
                ? RoleName.ROLE_ADMIN
                : RoleName.ROLE_USER;

        // Capitalize names properly
        String firstName = StringUtils.capitalize(request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(request.getLastName().trim().toLowerCase());

        User user = User.builder()
                .status(UserStatus.ACTIVE)
                .username(request.getUsername().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .password(request.getPassword())
                .firstName(firstName)
                .lastName(lastName)
                .phone(request.getPhone())
                .departmentId(request.getDepartmentId())
                .roles(Collections.singleton(roleService.findByName(roleName)))

                .build();
        User savedUser = userRepository.save(user);
        log.info("User successfully registered with username: {}", savedUser.getUsername());
        return UserMapper.toResponse(savedUser);

    }

    @Transactional
    public UserResponse update(UpdateUserRequest request) {
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("User", request.getId()));

        String firstName = StringUtils.capitalize(request.getFirstName().trim().toLowerCase());
        String lastName = StringUtils.capitalize(request.getLastName().trim().toLowerCase());

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setPhone(request.getPhone());
        existingUser.setDepartmentId(request.getDepartmentId());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(existingUser);
        log.info("User successfully updated with username: {}", savedUser.getUsername());
        return UserMapper.toResponse(savedUser);

    }


    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));
        userRepository.delete(user);
        log.info("User with id '{}' deleted successfully", id);
    }
    @Transactional(readOnly = true)
    public List<UserResponse> findAllByStatus(UserStatus status) {
        return UserMapper.toUserResposnseList(
                userRepository.findAllByStatus(status));
    }
}
