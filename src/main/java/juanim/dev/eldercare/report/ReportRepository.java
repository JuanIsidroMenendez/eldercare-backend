package juanim.dev.eldercare.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    // Listado de informes del residente, de reciente a antiguo.
    List<ReportEntity> findByResidentIdOrderByCreatedAtDesc(Long residentId);
}
