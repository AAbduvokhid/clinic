package uz.snow.clinic.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.department.model.dto.response.DepartmentResponse;
import uz.snow.clinic.department.model.entity.Department;
import uz.snow.clinic.department.service.DepartmentService;
import uz.snow.clinic.user.mapper.UserMapper;
import uz.snow.clinic.user.model.dto.request.UpdateUserRequest;
import uz.snow.clinic.user.model.dto.request.UserRegistrationRequest;
import uz.snow.clinic.user.model.dto.response.ApiResponse;
import uz.snow.clinic.user.model.dto.response.UserResponse;
import uz.snow.clinic.user.model.entity.Role;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.model.enums.UserStatus;
import uz.snow.clinic.user.service.RoleService;
import uz.snow.clinic.user.service.UserService;


import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> findByUsername(@PathVariable String username) {
        UserResponse response = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", response));

    }

    // GET /api/v1/users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        DepartmentResponse department = departmentService.findById(request.getDepartmentId());


        UserResponse savedUser = userService.register(request, department.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", savedUser));

    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> update(@Valid @RequestBody UpdateUserRequest request) {

        UserResponse updateUser = userService.update(request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updateUser));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAllByStatus(
            @PathVariable UserStatus status) {
        List<UserResponse> users = userService.findAllByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }
}