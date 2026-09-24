package com.rutaexpress.contracts.dto;

import com.rutaexpress.contracts.ShipmentStatus;
import java.time.Instant;

/**
 * Entrada del timeline de auditoría de un envío.
 */
public record AuditEntryDto(Long id, Long shipmentId, ShipmentStatus status, String actor, Instant occurredAt) {
}
