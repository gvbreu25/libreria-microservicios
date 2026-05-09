package ms_inventario.controller;

import jakarta.validation.Valid;
import ms_inventario.dto.InventarioDTO;
import ms_inventario.model.Inventario;
import ms_inventario.service.InventarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private static final Logger log =
            LoggerFactory.getLogger(InventarioController.class);

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        log.info("GET /api/v1/inventario");
        List<Inventario> lista = inventarioService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Inventario>> listarActivos() {
        log.info("GET /api/v1/inventario/activos");
        List<Inventario> lista = inventarioService.listarActivos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Inventario>> listarBajoStock() {
        log.info("GET /api/v1/inventario/bajo-stock");
        List<Inventario> lista = inventarioService.listarBajoStock();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/inventario/{}", id);
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<Inventario> buscarPorLibro(@PathVariable Long libroId) {
        log.info("GET /api/v1/inventario/libro/{}", libroId);
        return ResponseEntity.ok(inventarioService.buscarPorLibroId(libroId));
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@Valid @RequestBody InventarioDTO dto) {
        log.info("POST /api/v1/inventario");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO dto) {
        log.info("PUT /api/v1/inventario/{}", id);
        return ResponseEntity.ok(inventarioService.actualizar(id, dto));
    }

    @PatchMapping("/libro/{libroId}/descontar")
    public ResponseEntity<Inventario> descontarStock(
            @PathVariable Long libroId,
            @RequestParam Integer cantidad) {
        log.info("PATCH /api/v1/inventario/libro/{}/descontar?cantidad={}",
                libroId, cantidad);
        return ResponseEntity.ok(inventarioService.descontarStock(libroId, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/inventario/{}", id);
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
