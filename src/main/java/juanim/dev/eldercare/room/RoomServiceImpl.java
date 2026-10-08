package juanim.dev.eldercare.room;

import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
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
}