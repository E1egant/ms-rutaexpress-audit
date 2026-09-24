package com.rutaexpress.audit.domain;

import com.rutaexpress.contracts.ShipmentStatus;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

/** Filtros opcionales del timeline; cada criterio nulo no restringe. */
public final class AuditSpecifications {

    private AuditSpecifications() {
    }

    public static Specification<AuditEntry> filter(Long shipmentId, ShipmentStatus status,
            String actor, Instant from, Instant to) {
        return hasShipmentId(shipmentId)
                .and(hasStatus(status))
                .and(hasActor(actor))
                .and(occurredFrom(from))
                .and(occurredTo(to));
    }

    static Specification<AuditEntry> hasShipmentId(Long shipmentId) {
        return (root, query, cb) -> shipmentId == null ? null : cb.equal(root.get("shipmentId"), shipmentId);
    }

    static Specification<AuditEntry> hasStatus(ShipmentStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    static Specification<AuditEntry> hasActor(String actor) {
        return (root, query, cb) -> actor == null || actor.isBlank() ? null : cb.equal(root.get("actor"), actor);
    }

    static Specification<AuditEntry> occurredFrom(Instant from) {
        return (root, query, cb) -> from == null ? null
                : cb.greaterThanOrEqualTo(root.get("occurredAt"), from);
    }

    static Specification<AuditEntry> occurredTo(Instant to) {
        return (root, query, cb) -> to == null ? null
                : cb.lessThanOrEqualTo(root.get("occurredAt"), to);
    }
}
