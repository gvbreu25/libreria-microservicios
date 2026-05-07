package ms_resenas.controller;

import jakarta.validation.Valid;
import ms_resenas.dto.ResenaDTO;
import ms_resenas.model.Resena;
import ms_resenas.service.ResenaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resenas")
public class ResenaController {

    private static final Logger log =
            LoggerFactory.getLogger(ResenaController.class);

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<Resena>> listar() {
        log.info("GET /api/v1/resenas");
        List<Resena> lista = resenaService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Resena>> listarPorLibro(
            @PathVariable Long libroId) {
        log.info("GET /api/v1/resenas/libro/{}", libroId);
        List<Resena> lista = resenaService.listarPorLibro(libroId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        log.info("GET /api/v1/resenas/usuario/{}", usuarioId);
        List<Resena> lista = resenaService.listarPorUsuario(usuarioId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/resenas/{}", id);
        try {
            return ResponseEntity.ok(resenaService.buscarPorId(id));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        log.info("POST /api/v1/resenas");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(resenaService.crear(dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaDTO dto) {
        log.info("PUT /api/v1/resenas/{}", id);
        try {
            return ResponseEntity.ok(resenaService.actualizar(id, dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/resenas/{}", id);
        try {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}