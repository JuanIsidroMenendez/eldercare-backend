package juanim.dev.eldercare.report.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportDTORequest(
        @NotNull Long residentId,
        @NotBlank String content
) {}