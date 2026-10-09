package juanim.dev.eldercare.room;

import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomEntity toEntity(RoomDTORequest req) {
        return RoomEntity.builder()
                .number(req.number())
                .type(req.type())
                .build();
    }

    public void updateEntity(RoomEntity e, RoomDTORequest req) {
        e.setNumber(req.number());
        e.setType(req.type());
    }

    public RoomDTOResponse toResponse(RoomEntity e) {
        int capacity = e.getType() != null ? e.getType().getCapacity() : 0;
        int occupancy = e.getResidents() != null ? e.getResidents().size() : 0;
        return new RoomDTOResponse(
                e.getId(),
                e.getNumber(),
                e.getType(),
                e.getType() != null ? e.getType().getCapacity() : 0,
                occupancy,
                occupancy >= capacity
        );
    }
}