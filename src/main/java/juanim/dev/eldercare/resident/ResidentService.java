package juanim.dev.eldercare.resident;

import java.util.List;
import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;

public interface ResidentService {
    ResidentDTOResponse create(ResidentDTORequest request);
    List<ResidentDTOResponse> findAll();
    ResidentDTOResponse findById(Long id);
    ResidentDTOResponse update(Long id, ResidentDTORequest request);
    void delete(Long id);
}