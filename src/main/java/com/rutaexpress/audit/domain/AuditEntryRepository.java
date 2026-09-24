package com.rutaexpress.audit.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditEntryRepository
        extends JpaRepository<AuditEntry, Long>, JpaSpecificationExecutor<AuditEntry> {

    List<AuditEntry> findByShipmentIdOrderByOccurredAtAsc(Long shipmentId);
}
