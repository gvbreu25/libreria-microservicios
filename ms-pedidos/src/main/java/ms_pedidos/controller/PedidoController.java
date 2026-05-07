package ms_pedidos.controller;

import jakarta.validation.Valid;
import ms_pedidos.dto.PedidoDTO;
import ms_pedidos.model.Pedido;
import ms_pedidos.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        log.info("GET /api/v1/pedidos");
        List<Pedido> lista = pedidoService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/v1/pedidos/usuario/{}", usuarioId);
        List<Pedido> lista = pedidoService.listarPorUsuario(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> listarPorEstado(
            @PathVariable String estado) {
        log.info("GET /api/v1/pedidos/estado/{}", estado);
        try {
            List<Pedido> lista = pedidoService.listarPorEstado(
                    Pedido.EstadoPedido.valueOf(estado.toUpperCase()));
            if (lista.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            log.error("Estado inválido: {}", estado);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/pedidos/{}", id);
        try {
            return ResponseEntity.ok(pedidoService.buscarPorId(id));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody PedidoDTO dto) {
        log.info("POST /api/v1/pedidos");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(pedidoService.crear(dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        log.info("PATCH /api/v1/pedidos/{}/estado -> {}", id, estado);
        try {
            return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/pedidos/{}", id);
        try {
            pedidoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}