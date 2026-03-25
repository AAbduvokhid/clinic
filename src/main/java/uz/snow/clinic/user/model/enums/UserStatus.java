package uz.snow.clinic.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor

public enum UserStatus {
    ACTIVE("Active"),
    DISABLED("Disabled"),
    DELETED("Deleted");
    private final String displayName;
}
