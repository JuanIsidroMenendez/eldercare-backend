package juanim.dev.eldercare.report;

import jakarta.validation.Valid;
import juanim.dev.eldercare.report.dtos.ReportDTORequest;
import juanim.dev.eldercare.report.dtos.ReportDTOResponse;
import juanim.dev.eldercare.report.implementations.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    // Crea un informe.
    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.CREATED)
    public ReportDTOResponse create(@Valid @RequestBody ReportDTORequest request) {
        return service.create(request);
    }

    // Lista los informes de un residente.
    @GetMapping("/residents/{residentId}/reports")
    public List<ReportDTOResponse> findByResident(@PathVariable Long residentId) {
        return service.findByResident(residentId);
    }
}