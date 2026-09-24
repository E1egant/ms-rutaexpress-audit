package com.rutaexpress.audit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rutaexpress.contracts.ShipmentStatus;
import com.rutaexpress.contracts.dto.AuditEntryDto;
import com.rutaexpress.contracts.event.ShipmentEvent;
import com.rutaexpress.audit.domain.AuditEntry;
import com.rutaexpress.audit.domain.AuditEntryRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    AuditEntryRepository repository;
    @InjectMocks
    AuditService service;

    @Test
    void recordPersisteElEventoDelEnvio() {
        Instant now = Instant.parse("2026-09-21T12:00:00Z");

        service.record(new ShipmentEvent(UUID.randomUUID(), 7L, ShipmentStatus.ASSIGNED, "operador1", now));

        ArgumentCaptor<AuditEntry> captor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getShipmentId()).isEqualTo(7L);
        assertThat(captor.getValue().getStatus()).isEqualTo(ShipmentStatus.ASSIGNED);
        assertThat(captor.getValue().getActor()).isEqualTo("operador1");
        assertThat(captor.getValue().getOccurredAt()).isEqualTo(now);
    }

    @Test
    void listByShipmentUsaElOrdenCronologico() {
        AuditEntry entry = new AuditEntry();
        entry.setShipmentId(7L);
        entry.setStatus(ShipmentStatus.CREATED);
        entry.setOccurredAt(Instant.parse("2026-09-21T12:00:00Z"));
        when(repository.findByShipmentIdOrderByOccurredAtAsc(7L)).thenReturn(List.of(entry));

        assertThat(service.listByShipment(7L)).extracting(AuditEntryDto::status)
                .containsExactly(ShipmentStatus.CREATED);
    }

    @Test
    void listDevuelveTodasLasEntradas() {
        when(repository.findAll()).thenReturn(List.of(new AuditEntry(), new AuditEntry()));

        assertThat(service.list()).hasSize(2);
    }
}
