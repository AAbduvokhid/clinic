package uz.snow.clinic.visit.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PAID("Paid"),
    UNPAID("Unpaid"),
    PARTIALLY_PAID("Partially Paid");

    private final String displayName;
}

