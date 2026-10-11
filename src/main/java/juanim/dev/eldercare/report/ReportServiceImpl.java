package juanim.dev.eldercare.report;

import juanim.dev.eldercare.report.dtos.ReportDTORequest;
import juanim.dev.eldercare.report.dtos.ReportDTOResponse;
import juanim.dev.eldercare.report.implementations.ReportService;
import juanim.dev.eldercare.resident.ResidentEntity;
import juanim.dev.eldercare.resident.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;
    private final ReportMapper mapper;
    private final ResidentRepository residentRepository;

    @Override
    @Transactional
    public ReportDTOResponse create(ReportDTORequest request) {
        ResidentEntity resident = residentRepository.findById(request.residentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Residente no encontrado: " + request.residentId()));

        // El autor será el usuario autenticado.
        String author = SecurityContextHolder.getContext().getAuthentication().getName();

        ReportEntity report = ReportEntity.builder()
                .content(request.content())
                .author(author)
                .resident(resident)
                .build();

        return mapper.toResponse(repository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTOResponse> findByResident(Long residentId) {
        return repository.findByResidentIdOrderByCreatedAtDesc(residentId)
                .stream().map(mapper::toResponse).toList();
    }
}