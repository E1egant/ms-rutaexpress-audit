package com.rutaexpress.audit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rutaexpress.contracts.ShipmentStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Filtros del timeline contra H2 real (sin contexto web ni seguridad). */
@DataJpaTest
class AuditSearchTest {

    @Autowired
    AuditEntryRepository repository;

    @Test
    void filtraPorActorYEstado() {
        save(1L, ShipmentStatus.CREATED, "ana", "2026-09-20T10:00:00Z");
        save(1L, ShipmentStatus.ASSIGNED, "ana", "2026-09-20T11:00:00Z");
        save(2L, ShipmentStatus.CREATED, "luis", "2026-09-20T10:30:00Z");

        List<AuditEntry> result = repository.findAll(
                AuditSpecifications.filter(null, ShipmentStatus.CREATED, "ana", null, null));

        assertThat(result).extracting(AuditEntry::getShipmentId).containsExactly(1L);
    }

    @Test
    void filtraPorRangoDeFechas() {
        save(1L, ShipmentStatus.CREATED, "ana", "2026-09-20T10:00:00Z");
        save(1L, ShipmentStatus.DELIVERED, "ana", "2026-09-22T10:00:00Z");

        List<AuditEntry> result = repository.findAll(AuditSpecifications.filter(null, null, null,
                Instant.parse("2026-09-21T00:00:00Z"), Instant.parse("2026-09-23T00:00:00Z")));

        assertThat(result).extracting(AuditEntry::getStatus)
                .containsExactly(ShipmentStatus.DELIVERED);
    }

    @Test
    void sinFiltrosDevuelveTodo() {
        save(1L, ShipmentStatus.CREATED, null, "2026-09-20T10:00:00Z");
        save(2L, ShipmentStatus.CREATED, null, "2026-09-20T10:00:00Z");

        assertThat(repository.findAll(AuditSpecifications.filter(null, null, null, null, null)))
                .hasSize(2);
    }

    private void save(Long shipmentId, ShipmentStatus status, String actor, String occurredAt) {
        AuditEntry entry = new AuditEntry();
        entry.setShipmentId(shipmentId);
        entry.setStatus(status);
        entry.setActor(actor);
        entry.setOccurredAt(Instant.parse(occurredAt));
        repository.save(entry);
    }
}
