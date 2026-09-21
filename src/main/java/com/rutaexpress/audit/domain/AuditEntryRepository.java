package com.rutaexpress.audit.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEntryRepository extends JpaRepository<AuditEntry, Long> {

    List<AuditEntry> findByShipmentIdOrderByOccurredAtAsc(Long shipmentId);
}
