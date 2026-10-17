package juanim.dev.eldercare.resident;

import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;
import juanim.dev.eldercare.resident.valueObject.DependencyGrade;
import juanim.dev.eldercare.room.RoomEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResidentMapperTest {

    private ResidentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ResidentMapper();
    }

    private ResidentDTORequest sampleRequest() {
        return new ResidentDTORequest(
                "foto.jpg", "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Calle 1", "Oviedo", "33001", "José", "Hijo", "600111222", "Dr. Arduengo"
        );
    }

    @Test
    void toEntityMapsAllFields() {
        ResidentEntity entity = mapper.toEntity(sampleRequest());

        assertThat(entity.getFirstName()).isEqualTo("Iker");
        assertThat(entity.getLastName()).isEqualTo("Ardu Engo");
        assertThat(entity.getAge()).isEqualTo(85);
        assertThat(entity.getDependencyGrade()).isEqualTo(DependencyGrade.GRADO_I);
        assertThat(entity.getProfessional()).isEqualTo("Dr. Arduengo");
        assertThat(entity.getAddress().getStreet()).isEqualTo("Calle 1");
        assertThat(entity.getAddress().getCity()).isEqualTo("Oviedo");
        assertThat(entity.getReferenceRelative().getName()).isEqualTo("José");
        assertThat(entity.getReferenceRelative().getPhone()).isEqualTo("600111222");
    }

    @Test
    void toResponseMapsAllFieldsIncludingRoomAndDescription() {
        ResidentEntity entity = mapper.toEntity(sampleRequest());
        entity.setId(1L);
        entity.setRoom(RoomEntity.builder().number("101A").build());

        ResidentDTOResponse response = mapper.toResponse(entity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.firstName()).isEqualTo("Iker");
        assertThat(response.dependencyGrade()).isEqualTo(DependencyGrade.GRADO_I);
        assertThat(response.dependencyGradeDescription()).isEqualTo("Dependencia leve");
        assertThat(response.street()).isEqualTo("Calle 1");
        assertThat(response.familyName()).isEqualTo("José");
        assertThat(response.roomNumber()).isEqualTo("101A");
    }

    @Test
    void toResponseHandlesNullAddressRelativeAndRoom() {
        // Entidad "desnuda": sin dirección, sin familiar, sin habitación
        ResidentEntity entity = ResidentEntity.builder()
                .firstName("Ana")
                .lastName("García")
                .age(90)
                .dependencyGrade(DependencyGrade.SIN_GRADO)
                .build();

        ResidentDTOResponse response = mapper.toResponse(entity);

        // Los campos ausenten DEBEN lanzar un null.
        assertThat(response.firstName()).isEqualTo("Ana");
        assertThat(response.street()).isNull();
        assertThat(response.familyName()).isNull();
        assertThat(response.roomNumber()).isNull();
        assertThat(response.dependencyGradeDescription()).isEqualTo("Sin grado de dependencia");
    }

    @Test
    void updateEntityOverwritesFields() {
        ResidentEntity entity = mapper.toEntity(sampleRequest());

        ResidentDTORequest updated = new ResidentDTORequest(
                "nueva.jpg", "Marta", "López", 70, DependencyGrade.GRADO_III,
                "Calle 2", "Gijón", "33200", "Luis", "Hermano", "600999888", "Dra. Pérez"
        );

        mapper.updateEntity(entity, updated);

        assertThat(entity.getFirstName()).isEqualTo("Marta");
        assertThat(entity.getAge()).isEqualTo(70);
        assertThat(entity.getDependencyGrade()).isEqualTo(DependencyGrade.GRADO_III);
        assertThat(entity.getAddress().getCity()).isEqualTo("Gijón");
        assertThat(entity.getReferenceRelative().getName()).isEqualTo("Luis");
        assertThat(entity.getProfessional()).isEqualTo("Dra. Pérez");
    }
}