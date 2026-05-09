package ms_categorias.controller;

import jakarta.validation.Valid;
import ms_categorias.dto.CategoriaDTO;
import ms_categorias.model.Categoria;
import ms_categorias.service.CategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private static final Logger log =
            LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        log.info("GET /api/v1/categorias");
        List<Categoria> categorias = categoriaService.listarTodos();
        if (categorias.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> listarActivas() {
        log.info("GET /api/v1/categorias/activas");
        List<Categoria> categorias = categoriaService.listarActivos();
        if (categorias.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        log.info("GET /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaDTO dto) {
        log.info("POST /api/v1/categorias");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO dto) {
        log.info("PUT /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/categorias/{}", id);
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
