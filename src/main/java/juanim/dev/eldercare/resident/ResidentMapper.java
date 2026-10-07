package juanim.dev.eldercare.resident;


import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;

import org.springframework.stereotype.Component;

@Component
public class ResidentMapper {

    public ResidentEntity toEntity(ResidentDTORequest req) {
        return ResidentEntity.builder()
                .photo(req.photo())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .age(req.age())
                .dependencyGrade(req.dependencyGrade())
                .address(Address.builder()
                        .street(req.street()).city(req.city()).postalCode(req.postalCode())
                        .build())
                .referenceRelative(ReferenceRelative.builder()
                        .name(req.familyName()).relationship(req.relationship()).phone(req.phone())
                        .build())
                .professional(req.professional())
                .build();
    }

    public void updateEntity(ResidentEntity e, ResidentDTORequest req) {
        e.setPhoto(req.photo());
        e.setFirstName(req.firstName());
        e.setLastName(req.lastName());
        e.setAge(req.age());
        e.setDependencyGrade(req.dependencyGrade());
        e.setAddress(Address.builder()
                .street(req.street()).city(req.city()).postalCode(req.postalCode()).build());
        e.setReferenceRelative(ReferenceRelative.builder()
                .name(req.familyName()).relationship(req.relationship()).phone(req.phone()).build());
        e.setProfessional(req.professional());
    }

    public ResidentDTOResponse toResponse(ResidentEntity e) {
        Address a = e.getAddress() != null ? e.getAddress() : Address.builder().build();
        ReferenceRelative f = e.getReferenceRelative() != null ? e.getReferenceRelative() : ReferenceRelative.builder().build();
        return new ResidentDTOResponse(
                e.getId(), e.getPhoto(), e.getFirstName(), e.getLastName(), e.getAge(),
                e.getDependencyGrade(),
                e.getDependencyGrade() != null ? e.getDependencyGrade().getDescription() : null,
                a.getStreet(), a.getCity(), a.getPostalCode(),
                f.getName(), f.getRelationship(), f.getPhone(),
                e.getProfessional()
        );
    }
}