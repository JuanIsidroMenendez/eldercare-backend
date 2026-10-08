package juanim.dev.eldercare.resident.dtos;

import juanim.dev.eldercare.resident.objectValue.DependencyGrade;

public record ResidentDTOResponse(
    Long id,
    String photo,
    String firstName,
    String lastName,
    Integer age,
    DependencyGrade dependencyGrade,
    String dependencyGradeDescription,
    String street,
    String city,
    String postalCode,
    String familyName,
    String relationship,
    String phone,
    String professional
    
){}
