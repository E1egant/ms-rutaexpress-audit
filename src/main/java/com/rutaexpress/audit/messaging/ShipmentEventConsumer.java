package com.rutaexpress.audit.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rutaexpress.audit.service.AuditService;
import com.rutaexpress.contracts.MessagingConstants;
import com.rutaexpress.contracts.event.ShipmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ShipmentEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final AuditService service;

    public ShipmentEventConsumer(ObjectMapper objectMapper, AuditService service) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @KafkaListener(topics = {
            MessagingConstants.SHIPMENT_EVENTS_TOPIC,
            MessagingConstants.SHIPMENT_EVENTS_TOPIC_V2 }, groupId = "audit")
    public void onEvent(String json) {
        try {
            ShipmentEvent event = objectMapper.readValue(json, ShipmentEvent.class);
            service.record(event);
        } catch (JsonProcessingException e) {
            log.error("No se pudo deserializar el evento de envío: {}", json, e);
        }
    }
}
