package juanim.dev.eldercare.room;

import juanim.dev.eldercare.resident.ResidentEntity;
import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoomMapperTest {

    private RoomMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RoomMapper();
    }

    @Test
    void toEntityMapsFields() {
        RoomEntity entity = mapper.toEntity(new RoomDTORequest("101A", RoomType.INDIVIDUAL));

        assertThat(entity.getNumber()).isEqualTo("101A");
        assertThat(entity.getType()).isEqualTo(RoomType.INDIVIDUAL);
    }

    @Test
    void toResponseComputesCapacityAndOccupancy() {
     
        RoomEntity entity = RoomEntity.builder()
                .id(1L)
                .number("201B")
                .type(RoomType.DOUBLE)
                .residents(List.of(new ResidentEntity()))
                .build();

        RoomDTOResponse response = mapper.toResponse(entity);

        assertThat(response.capacity()).isEqualTo(2);
        assertThat(response.occupancy()).isEqualTo(1);
        assertThat(response.full()).isFalse();
    }

    @Test
    void toResponseMarksFullWhenOccupancyReachesCapacity() {
        
        RoomEntity entity = RoomEntity.builder()
                .number("202B")
                .type(RoomType.DOUBLE)
                .residents(List.of(new ResidentEntity(), new ResidentEntity()))
                .build();

        RoomDTOResponse response = mapper.toResponse(entity);

        assertThat(response.occupancy()).isEqualTo(2);
        assertThat(response.full()).isTrue();
    }

    @Test
    void toResponseHandlesNullTypeAndResidents() {
      
        RoomEntity entity = new RoomEntity();
        entity.setResidents(null); 

        RoomDTOResponse response = mapper.toResponse(entity);

        assertThat(response.capacity()).isZero();
        assertThat(response.occupancy()).isZero();
    }

    @Test
    void updateEntityOverwritesFields() {
        RoomEntity entity = mapper.toEntity(new RoomDTORequest("101A", RoomType.INDIVIDUAL));

        mapper.updateEntity(entity, new RoomDTORequest("305C", RoomType.DOUBLE));

        assertThat(entity.getNumber()).isEqualTo("305C");
        assertThat(entity.getType()).isEqualTo(RoomType.DOUBLE);
    }
}