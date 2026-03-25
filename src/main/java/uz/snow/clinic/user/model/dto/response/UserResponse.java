package uz.snow.clinic.user.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import uz.snow.clinic.user.model.entity.Role;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.model.enums.UserStatus;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String username;

    private String firstName;
    private String lastName;
    private String phone;
    private Long departmentId;
    private UserStatus status;
    private Set<RoleName> roles;
    private boolean isAdmin;

    // Added audit fields — useful for admin panels and debugging
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
