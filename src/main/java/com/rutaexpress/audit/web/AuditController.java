package com.rutaexpress.audit.web;

import com.rutaexpress.audit.service.AuditService;
import com.rutaexpress.contracts.ApiPaths;
import com.rutaexpress.contracts.dto.AuditEntryDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUDIT)
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditEntryDto> list(@RequestParam(required = false) Long shipmentId) {
        return shipmentId == null ? service.list() : service.listByShipment(shipmentId);
    }
}
