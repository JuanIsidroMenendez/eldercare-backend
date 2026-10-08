package juanim.dev.eldercare.resident.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import juanim.dev.eldercare.resident.valueObject.DependencyGrade;

public record ResidentDTORequest(
    String photo,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull @Positive Integer age,
    @NotNull DependencyGrade dependencyGrade,
    // Domicilio.
    String street,
    String city,
    String postalCode,
    // Familiar de referencia.
    String familyName,
    String relationship,
    String phone,
    // Profesional
    String professional
) {}
