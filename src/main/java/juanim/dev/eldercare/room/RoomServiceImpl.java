package juanim.dev.eldercare.room;

import juanim.dev.eldercare.resident.ResidentEntity;
import juanim.dev.eldercare.resident.ResidentRepository;
import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import juanim.dev.eldercare.room.exceptions.RoomFullException;
import juanim.dev.eldercare.room.implementations.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;
    private final ResidentRepository residentRepository;

    @Override
    @Transactional
    public RoomDTOResponse create(RoomDTORequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTOResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDTOResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public RoomDTOResponse update(Long id, RoomDTORequest request) {
        RoomEntity entity = getOrThrow(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private RoomEntity getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Habitación no encontrada: " + id));
    }
    // Asigna un residente a la habitación, respetando la capacidad.
    @Override
    @Transactional
    public RoomDTOResponse assignResident(Long roomId, Long residentId) {
        RoomEntity room = getOrThrow(roomId);
        ResidentEntity resident = getResidentOrThrow(residentId);

        if (room.getResidents().size() >= room.getType().getCapacity()) {
            throw new RoomFullException("habitación completa");
        }
        resident.setRoom(room);
        residentRepository.save(resident);
        room.getResidents().add(resident);   
        return mapper.toResponse(room);
    }
    // Retira a un residente de una habitación.
    @Override
    @Transactional
    public RoomDTOResponse removeResident(Long roomId, Long residentId) {
        RoomEntity room = getOrThrow(roomId);
        ResidentEntity resident = getResidentOrThrow(residentId);

        if (resident.getRoom() != null && resident.getRoom().getId().equals(roomId)) {
            resident.setRoom(null);
            residentRepository.save(resident);
            room.getResidents().removeIf(r -> r.getId().equals(residentId));
        }
        return mapper.toResponse(room);
    }
    private ResidentEntity getResidentOrThrow(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Residente no encontrado: " + id));
    }
}
