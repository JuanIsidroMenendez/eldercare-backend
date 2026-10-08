package juanim.dev.eldercare.resident;


import jakarta.validation.Valid;
import juanim.dev.eldercare.resident.dtos.ResidentDTORequest;
import juanim.dev.eldercare.resident.dtos.ResidentDTOResponse;
import juanim.dev.eldercare.resident.implementations.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/residents")
@RequiredArgsConstructor 
public class ResidentController {

    private final ResidentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidentDTOResponse create(@Valid @RequestBody ResidentDTORequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<ResidentDTOResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResidentDTOResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ResidentDTOResponse update(@PathVariable Long id, @Valid @RequestBody ResidentDTORequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }  
}
