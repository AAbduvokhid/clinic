package uz.snow.clinic.department.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DepartmentName {

    LABORATORY("Laboratory"),
    ULTRASOUND("Ultrasound"),
    X_RAY("X-Ray"),
    MRT("MRI"),
    MCKT("MSCT"),
    NEUROLOGY("Neurology"),
    UROLOGY("Urology"),
    GYNECOLOGY("Gynecology"),
    SURGERY("Surgery"),
    TRAUMATOLOGY("Traumatology"),
    LOR("ENT"),                        // ENT = Ear, Nose and Throat (international term for LOR)
    CARDIOLOGY("Cardiology"),
    THERAPY("Therapy"),
    ENDOCRINOLOGY("Endocrinology"),
    GASTROENTEROLOGY("Gastroenterology"),
    PHYSIOLOGY("Physiotherapy"),
    REGISTRATION("Registration");

    private final String displayName;
}