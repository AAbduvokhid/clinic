package uz.snow.clinic.user.mapper;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.user.model.dto.response.UserResponse;
import uz.snow.clinic.user.model.entity.Role;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    // Converts a User entity to UserResponse DTO
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .departmentId(user.getDepartmentId())
                .status(user.getStatus())
                .roles(extractRoleName(user))
                .isAdmin(checkIsAdmin(user))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Converts a list of User entities to list of UserResponse DTOs
    public static List<UserResponse> toUserResposnseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    private static Set<RoleName> extractRoleName(User user) {
        return user.getRoles().stream()
                .map(Role::getName) //or role -> role.getName()
                .collect(Collectors.toSet());
    }

    private boolean checkIsAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role->role.getName() == RoleName.ROLE_ADMIN);
    }
}
