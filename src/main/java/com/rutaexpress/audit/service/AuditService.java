package com.rutaexpress.audit.service;

import com.rutaexpress.audit.domain.AuditEntry;
import com.rutaexpress.audit.domain.AuditEntryRepository;
import com.rutaexpress.audit.domain.AuditSpecifications;
import com.rutaexpress.contracts.ShipmentStatus;
import com.rutaexpress.contracts.dto.AuditEntryDto;
import com.rutaexpress.contracts.event.ShipmentEvent;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditEntryRepository repository;

    public AuditService(AuditEntryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void record(ShipmentEvent event) {
        AuditEntry entry = new AuditEntry();
        entry.setShipmentId(event.shipmentId());
        entry.setStatus(event.status());
        entry.setActor(event.actor());
        entry.setOccurredAt(event.occurredAt());
        repository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDto> list() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDto> listByShipment(Long shipmentId) {
        return repository.findByShipmentIdOrderByOccurredAtAsc(shipmentId).stream()
                .map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AuditEntryDto> search(Long shipmentId, ShipmentStatus status, String actor,
            Instant from, Instant to) {
        return repository
                .findAll(AuditSpecifications.filter(shipmentId, status, actor, from, to),
                        Sort.by("occurredAt").ascending())
                .stream().map(this::toDto).toList();
    }

    private AuditEntryDto toDto(AuditEntry entry) {
        return new AuditEntryDto(entry.getId(), entry.getShipmentId(),
                entry.getStatus(), entry.getActor(), entry.getOccurredAt());
    }
}
