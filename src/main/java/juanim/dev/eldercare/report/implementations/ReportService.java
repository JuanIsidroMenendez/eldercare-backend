package juanim.dev.eldercare.report.implementations;

import juanim.dev.eldercare.report.dtos.ReportDTORequest;
import juanim.dev.eldercare.report.dtos.ReportDTOResponse;

import java.util.List;

public interface ReportService {
    ReportDTOResponse create(ReportDTORequest request);
    List<ReportDTOResponse> findByResident(Long residentId);
}