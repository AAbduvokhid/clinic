package uz.snow.clinic.util;

import lombok.experimental.UtilityClass;
import uz.snow.clinic.user.model.entity.User;
import uz.snow.clinic.user.model.enums.RoleName;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {
    public static boolean isAdmin(Optional<User> userOptional) {
        return userOptional.get().getRoles().stream()
                .anyMatch(role -> role.getName().name().equals(RoleName.ROLE_ADMIN.name()));
    }

    public static Set<RoleName> getUserRoles(final User user) {
        return user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}
