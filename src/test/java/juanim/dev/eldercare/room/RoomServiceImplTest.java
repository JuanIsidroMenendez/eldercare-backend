package juanim.dev.eldercare.room;

import juanim.dev.eldercare.resident.ResidentEntity;
import juanim.dev.eldercare.resident.ResidentRepository;
import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import juanim.dev.eldercare.room.exceptions.RoomFullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository repository;

    @Mock
    private RoomMapper mapper;

    @Mock
    private ResidentRepository residentRepository;

    @InjectMocks
    private RoomServiceImpl service;

    @Test
    void createSavesAndReturnsRoom() {
        RoomDTORequest request = new RoomDTORequest("101A", RoomType.INDIVIDUAL);
        RoomEntity entity = RoomEntity.builder().number("101A").type(RoomType.INDIVIDUAL).build();
        RoomEntity saved = RoomEntity.builder().id(1L).number("101A").type(RoomType.INDIVIDUAL).build();
        RoomDTOResponse response = new RoomDTOResponse(1L, "101A", RoomType.INDIVIDUAL, 1, 0, false);

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        RoomDTOResponse result = service.create(request);

        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(entity);
    }

    @Test
    void findByIdThrowsWhenRoomNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void assignResidentAddsResidentWhenRoomNotFull() {
        
        RoomEntity room = RoomEntity.builder().id(1L).number("201B").type(RoomType.DOUBLE).build();
        ResidentEntity resident = ResidentEntity.builder().id(2L).build();
        RoomDTOResponse response = new RoomDTOResponse(1L, "201B", RoomType.DOUBLE, 2, 1, false);

        when(repository.findById(1L)).thenReturn(Optional.of(room));
        when(residentRepository.findById(2L)).thenReturn(Optional.of(resident));
        when(mapper.toResponse(room)).thenReturn(response);

        RoomDTOResponse result = service.assignResident(1L, 2L);

        assertThat(result.number()).isEqualTo("201B");
        assertThat(resident.getRoom()).isEqualTo(room);     
        assertThat(room.getResidents()).contains(resident); 
        verify(residentRepository).save(resident);
    }

    @Test
    void assignResidentThrowsWhenRoomIsFull() {
       
        RoomEntity room = RoomEntity.builder()
                .id(1L).number("101A").type(RoomType.INDIVIDUAL)
                .residents(new ArrayList<>(List.of(new ResidentEntity())))
                .build();
        ResidentEntity resident = ResidentEntity.builder().id(2L).build();

        when(repository.findById(1L)).thenReturn(Optional.of(room));
        when(residentRepository.findById(2L)).thenReturn(Optional.of(resident));

        assertThatThrownBy(() -> service.assignResident(1L, 2L))
                .isInstanceOf(RoomFullException.class)
                .hasMessageContaining("completa");

        verify(residentRepository, never()).save(any());
    }

    @Test
    void removeResidentDetachesResident() {
        RoomEntity room = RoomEntity.builder().id(1L).number("201B").type(RoomType.DOUBLE).build();
        ResidentEntity resident = ResidentEntity.builder().id(2L).build();
        resident.setRoom(room);
        room.getResidents().add(resident);
        RoomDTOResponse response = new RoomDTOResponse(1L, "201B", RoomType.DOUBLE, 2, 0, false);

        when(repository.findById(1L)).thenReturn(Optional.of(room));
        when(residentRepository.findById(2L)).thenReturn(Optional.of(resident));
        when(mapper.toResponse(room)).thenReturn(response);

        service.removeResident(1L, 2L);

        assertThat(resident.getRoom()).isNull();              
        
        assertThat(room.getResidents()).doesNotContain(resident);
        verify(residentRepository).save(resident);
    }
}