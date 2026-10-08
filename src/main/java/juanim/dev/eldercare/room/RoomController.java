package juanim.dev.eldercare.room;

import jakarta.validation.Valid;
import juanim.dev.eldercare.room.dtos.RoomDTORequest;
import juanim.dev.eldercare.room.dtos.RoomDTOResponse;
import juanim.dev.eldercare.room.implementations.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/rooms")
@RequiredArgsConstructor 
public class RoomController {

    private final RoomService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDTOResponse create(@Valid @RequestBody RoomDTORequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<RoomDTOResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RoomDTOResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RoomDTOResponse update(@PathVariable Long id, @Valid @RequestBody RoomDTORequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
