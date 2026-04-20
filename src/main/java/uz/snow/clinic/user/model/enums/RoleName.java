package uz.snow.clinic.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleName{
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");
    private final String displayName;
}
