package juanim.dev.eldercare.resident;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;
import juanim.dev.eldercare.resident.implementations.ResidentService;

import java.util.List;

@Service
@RequiredArgsConstructor   // Inyección de repo por constructor (Lombook) para que funcione el servicio.
public class ResidentServiceImpl implements ResidentService {

    private final ResidentRepository repository;
    private final ResidentMapper mapper;

    @Override
    @Transactional
    public ResidentDTOResponse create(ResidentDTORequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResidentDTOResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResidentDTOResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public ResidentDTOResponse update(Long id, ResidentDTORequest request) {
        ResidentEntity entity = getOrThrow(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }
    // TEMPORAL: Hasta GlobalException o concretos.
    private ResidentEntity getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Residente no encontrado: " + id));
    }
}