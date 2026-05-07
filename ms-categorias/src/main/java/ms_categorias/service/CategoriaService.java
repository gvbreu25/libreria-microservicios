package ms_categorias.service;

import ms_categorias.dto.CategoriaDTO;
import ms_categorias.model.Categoria;
import ms_categorias.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private static final Logger log =
            LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        log.info("Listando todas las categorias");
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarActivos() {
        log.info("Listando categorias activas");
        return categoriaRepository.findByActivoTrue();
    }

    public Categoria buscarPorId(Long id) {
        log.info("Buscando categoria con id: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria no encontrada con id: {}", id);
                    return new RuntimeException("Categoria no encontrada con id: " + id);
                });
    }

    public Categoria crear(CategoriaDTO dto) {
        log.info("Creando categoria: {}", dto.getNombre());
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            log.warn("Categoria ya existe: {}", dto.getNombre());
            throw new RuntimeException("La categoria ya existe: " + dto.getNombre());
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoria creada con id: {}", guardada.getId());
        return guardada;
    }

    public Categoria actualizar(Long id, CategoriaDTO dto) {
        log.info("Actualizando categoria con id: {}", id);
        Categoria categoria = buscarPorId(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        log.info("Eliminando categoria con id: {}", id);
        Categoria categoria = buscarPorId(id);
        categoriaRepository.deleteById(categoria.getId());
        log.info("Categoria eliminada con id: {}", id);
    }
}