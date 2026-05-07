package ms_carrito.controller;

import jakarta.validation.Valid;
import ms_carrito.dto.ItemCarritoDTO;
import ms_carrito.model.ItemCarrito;
import ms_carrito.service.ItemCarritoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carrito")
public class ItemCarritoController {

    private static final Logger log =
            LoggerFactory.getLogger(ItemCarritoController.class);

    @Autowired
    private ItemCarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<ItemCarrito>> listar() {
        log.info("GET /api/v1/carrito");
        List<ItemCarrito> lista = carritoService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ItemCarrito>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/v1/carrito/usuario/{}", usuarioId);
        List<ItemCarrito> lista = carritoService.listarPorUsuario(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCarrito> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/carrito/{}", id);
        try {
            return ResponseEntity.ok(carritoService.buscarPorId(id));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ItemCarrito> agregar(
            @Valid @RequestBody ItemCarritoDTO dto) {
        log.info("POST /api/v1/carrito");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(carritoService.agregar(dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCarrito> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCarritoDTO dto) {
        log.info("PUT /api/v1/carrito/{}", id);
        try {
            return ResponseEntity.ok(carritoService.actualizar(id, dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/carrito/{}", id);
        try {
            carritoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciar(@PathVariable Long usuarioId) {
        log.info("DELETE /api/v1/carrito/usuario/{}/vaciar", usuarioId);
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
