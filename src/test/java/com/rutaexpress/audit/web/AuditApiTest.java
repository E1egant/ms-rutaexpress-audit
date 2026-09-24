package com.rutaexpress.audit.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** Perfil por defecto (dev): sin seguridad; el listener Kafka no arranca sin broker. */
@SpringBootTest(properties = {"spring.kafka.listener.auto-startup=false"})
@AutoConfigureMockMvc
class AuditApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listaSinFiltrosResponde200() throws Exception {
        mvc.perform(get("/api/audit")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void filtrosResponden200() throws Exception {
        mvc.perform(get("/api/audit")
                        .param("shipmentId", "7")
                        .param("status", "CREATED")
                        .param("actor", "ana")
                        .param("from", "2026-09-01T00:00:00Z")
                        .param("to", "2026-09-30T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void fechaInvalidaRetorna400() throws Exception {
        mvc.perform(get("/api/audit").param("from", "no-es-fecha"))
                .andExpect(status().isBadRequest());
    }
}
