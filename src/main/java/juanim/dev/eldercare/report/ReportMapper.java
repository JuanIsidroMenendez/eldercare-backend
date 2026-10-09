package juanim.dev.eldercare.report;

import juanim.dev.eldercare.report.dtos.ReportDTOResponse;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportDTOResponse toResponse(ReportEntity e) {
        var resident = e.getResident();
        return new ReportDTOResponse(
                e.getId(),
                e.getContent(),
                e.getAuthor(),
                e.getCreatedAt(),
                resident != null ? resident.getId() : null,
                resident != null ? resident.getFirstName() + " " + resident.getLastName() : null
        );
    }
}