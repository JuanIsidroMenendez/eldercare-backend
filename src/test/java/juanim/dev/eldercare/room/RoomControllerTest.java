package juanim.dev.eldercare.room;

import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import juanim.dev.eldercare.room.implementations.RoomService;
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
class RoomControllerTest {

    @Mock
    private RoomService service;

    @InjectMocks
    private RoomController controller;

    // Respuesta de ejemplo.
    private RoomDTOResponse sampleResponse() {
        return new RoomDTOResponse(1L, "101A", RoomType.INDIVIDUAL, 1, 0, false);
    }

    private RoomDTORequest sampleRequest() {
        return new RoomDTORequest("101A", RoomType.INDIVIDUAL);
    }

    @Test
    void createDelegatesToService() {
        RoomDTORequest request = sampleRequest();
        when(service.create(request)).thenReturn(sampleResponse());

        RoomDTOResponse result = controller.create(request);

        assertThat(result.number()).isEqualTo("101A");
        verify(service).create(request);
    }

    @Test
    void findAllDelegatesToService() {
        when(service.findAll()).thenReturn(List.of(sampleResponse()));

        List<RoomDTOResponse> result = controller.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).type()).isEqualTo(RoomType.INDIVIDUAL);
        verify(service).findAll();
    }

    @Test
    void findByIdDelegatesToService() {
        when(service.findById(1L)).thenReturn(sampleResponse());

        RoomDTOResponse result = controller.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        verify(service).findById(1L);
    }

    @Test
    void updateDelegatesToService() {
        RoomDTORequest request = sampleRequest();
        when(service.update(1L, request)).thenReturn(sampleResponse());

        RoomDTOResponse result = controller.update(1L, request);

        assertThat(result.id()).isEqualTo(1L);
        verify(service).update(1L, request);
    }

    @Test
    void deleteDelegatesToService() {
        controller.delete(1L);

        verify(service).delete(1L);
    }

    @Test
    void assignResidentDelegatesToService() {
       
        when(service.assignResident(1L, 2L)).thenReturn(sampleResponse());

        RoomDTOResponse result = controller.assignResident(1L, 2L);

        assertThat(result.number()).isEqualTo("101A");
        verify(service).assignResident(1L, 2L);
    }

    @Test
    void removeResidentDelegatesToService() {
        when(service.removeResident(1L, 2L)).thenReturn(sampleResponse());

        RoomDTOResponse result = controller.removeResident(1L, 2L);

        assertThat(result.id()).isEqualTo(1L);
        verify(service).removeResident(1L, 2L);
    }
}