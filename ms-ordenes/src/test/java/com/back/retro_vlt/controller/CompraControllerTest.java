package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.CompraDto;
import com.back.retro_vlt.dto.PagoDto;
import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.service.OrdenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompraController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdenService ordenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Orden orden;

    @BeforeEach
    void setUp() {
        orden = new Orden();
        orden.setId(1L);
        orden.setNumeroOrden("#20261123-1001");
        orden.setEstado("PENDIENTE_PAGO");
        orden.setTotal(30000.0);
        orden.setMetodoPago("WEBPAY");
        orden.setFecha(LocalDateTime.now());
    }

    @Test
    @DisplayName("Creación de orden de compra desde el carrito / checkout (201 Created)")
    void testProcesarCompraExitoso() throws Exception {
        CompraDto dto = new CompraDto();
        dto.setMetodoPago("WEBPAY");

        when(ordenService.procesarCompra(any(CompraDto.class))).thenReturn(orden);

        mockMvc.perform(post("/api/compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ordenId").value(1))
                .andExpect(jsonPath("$.numeroOrden").value("#20261123-1001"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE_PAGO"))
                .andExpect(jsonPath("$.total").value(30000.0));
    }

    @Test
    @DisplayName("Aprobación de pago con descuento de stock exitoso (200 OK)")
    void testProcesarPagoAprobado() throws Exception {
        orden.setEstado("PAGADO");
        PagoDto pagoDto = new PagoDto();
        pagoDto.setSimularError(false);
        pagoDto.setMetodoPago("WEBPAY");

        when(ordenService.procesarPago(eq(1L), any(PagoDto.class))).thenReturn(orden);

        mockMvc.perform(post("/api/compra/1/pagar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.estado").value("PAGADO"))
                .andExpect(jsonPath("$.numeroOrden").value("#20261123-1001"));
    }
}
