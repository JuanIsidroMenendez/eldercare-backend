package juanim.dev.eldercare.report.dtos;

import java.time.LocalDateTime;

public record ReportDTOResponse(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt,
        Long residentId,
        String residentName     
) {}