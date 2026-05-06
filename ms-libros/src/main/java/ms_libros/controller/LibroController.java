package ms_libros.controller;

import jakarta.validation.Valid;
import ms_libros.dto.LibroDTO;
import ms_libros.model.Libro;
import ms_libros.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    private static final Logger log =
            LoggerFactory.getLogger(LibroController.class);

    @Autowired
    private LibroService libroService;

    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        log.info("GET /api/v1/libros");
        List<Libro> libros = libroService.listarTodos();
        if (libros.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Libro>> listarActivos() {
        log.info("GET /api/v1/libros/activos");
        List<Libro> libros = libroService.listarActivos();
        if (libros.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/libros/{}", id);
        try {
            return ResponseEntity.ok(libroService.buscarPorId(id));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@Valid @RequestBody LibroDTO dto) {
        log.info("POST /api/v1/libros");
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(libroService.crear(dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LibroDTO dto) {
        log.info("PUT /api/v1/libros/{}", id);
        try {
            return ResponseEntity.ok(libroService.actualizar(id, dto));
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/libros/{}", id);
        try {
            libroService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}