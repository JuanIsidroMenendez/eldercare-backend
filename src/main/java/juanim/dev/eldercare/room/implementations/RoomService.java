package juanim.dev.eldercare.room.implementations;

import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;

import java.util.List;

public interface RoomService {
    RoomDTOResponse create(RoomDTORequest request);
    List<RoomDTOResponse> findAll();
    RoomDTOResponse findById(Long id);
    RoomDTOResponse update(Long id, RoomDTORequest request);
    void delete(Long id);
}