package juanim.dev.eldercare.resident;

import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;
import juanim.dev.eldercare.resident.implementations.ResidentService;
import juanim.dev.eldercare.resident.valueObject.DependencyGrade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResidentControllerTest {

    @Mock
    private ResidentService service;

    @InjectMocks
    private ResidentController controller;

    // Respuesta de ejemplo.
    private ResidentDTOResponse sampleResponse() {
        return new ResidentDTOResponse(
                1L, null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Dependencia leve", "Calle 1", "Oviedo", "33001",
                "José", "Hijo", "600111222", "Dr. Arduengo", "101A"
        );
    }

    private ResidentDTORequest sampleRequest() {
        return new ResidentDTORequest(
                null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Calle 1", "Oviedo", "33001", "José", "Hijo", "600111222", "Dr. Arduengo"
        );
    }

    @Test
    void findAllDelegatesToService() {
        when(service.findAll()).thenReturn(List.of(sampleResponse()));

        List<ResidentDTOResponse> result = controller.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).roomNumber()).isEqualTo("101A"); // accessor de record
        verify(service).findAll();
    }

    @Test
    void findByIdDelegatesToService() {
        when(service.findById(1L)).thenReturn(sampleResponse());

        ResidentDTOResponse result = controller.findById(1L);

        assertThat(result.firstName()).isEqualTo("Iker");
        verify(service).findById(1L);
    }

    @Test
    void createDelegatesToService() {
        ResidentDTORequest request = sampleRequest();
        when(service.create(request)).thenReturn(sampleResponse());

        ResidentDTOResponse result = controller.create(request);

        assertThat(result.id()).isEqualTo(1L);
        verify(service).create(request);
    }

    @Test
    void updateDelegatesToService() {
        ResidentDTORequest request = sampleRequest();
        when(service.update(1L, request)).thenReturn(sampleResponse());

        ResidentDTOResponse result = controller.update(1L, request);

        assertThat(result.id()).isEqualTo(1L);
        verify(service).update(1L, request);
    }

    @Test
    void deleteDelegatesToService() {
        controller.delete(1L);

        verify(service).delete(1L);
    }
}