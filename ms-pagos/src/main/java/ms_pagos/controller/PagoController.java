package ms_pagos.controller;

import jakarta.validation.Valid;
import ms_pagos.dto.PagoDTO;
import ms_pagos.model.Pago;
import ms_pagos.service.PagoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private static final Logger log =
            LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        log.info("GET /api/v1/pagos");
        List<Pago> lista = pagoService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/v1/pagos/usuario/{}", usuarioId);
        List<Pago> lista = pagoService.listarPorUsuario(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<Pago>> listarPorPedido(
            @PathVariable Long pedidoId) {
        log.info("GET /api/v1/pagos/pedido/{}", pedidoId);
        List<Pago> lista = pagoService.listarPorPedido(pedidoId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/pagos/{}", id);
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pago> procesar(@Valid @RequestBody PagoDTO dto) {
        log.info("POST /api/v1/pagos");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.procesar(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        log.info("PATCH /api/v1/pagos/{}/estado -> {}", id, estado);
        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/pagos/{}", id);
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
