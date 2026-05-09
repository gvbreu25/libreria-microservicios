package ms_inventario.service;

import ms_inventario.dto.InventarioDTO;
import ms_inventario.exception.RecursoNoEncontradoException;
import ms_inventario.exception.ReglaNegocioException;
import ms_inventario.model.Inventario;
import ms_inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class InventarioService {

    private static final Logger log =
            LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarTodos() {
        log.info("Listando todo el inventario");
        return inventarioRepository.findAll();
    }

    public List<Inventario> listarActivos() {
        log.info("Listando inventario activo");
        return inventarioRepository.findByActivoTrue();
    }

    public List<Inventario> listarBajoStock() {
        log.info("Listando libros con bajo stock");
        return inventarioRepository.findByCantidadLessThan(5);
    }

    public Inventario buscarPorId(Long id) {
        log.info("Buscando inventario con id: {}", id);
        return inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Inventario no encontrado con id: " + id);
                });
    }

    public Inventario buscarPorLibroId(Long libroId) {
        log.info("Buscando inventario del libro id: {}", libroId);
        return inventarioRepository.findByLibroId(libroId)
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado para libro id: {}", libroId);
                    return new RecursoNoEncontradoException(
                            "Inventario no encontrado para libro id: " + libroId);
                });
    }

    public Inventario crear(InventarioDTO dto) {
        log.info("Creando inventario para libro id: {}", dto.getLibroId());
        if (inventarioRepository.existsByLibroId(dto.getLibroId())) {
            log.warn("Ya existe inventario para libro id: {}", dto.getLibroId());
            throw new ReglaNegocioException(
                    "Ya existe inventario para el libro id: " + dto.getLibroId());
        }
        if (dto.getCantidad() == null || dto.getCantidad() < 0) {
            throw new ReglaNegocioException(
                    "La cantidad de stock no puede ser negativa");
        }
        Inventario inventario = new Inventario();
        inventario.setLibroId(dto.getLibroId());
        inventario.setCantidad(dto.getCantidad());
        if (dto.getCantidadMinima() != null) {
            inventario.setCantidadMinima(dto.getCantidadMinima());
        }
        Inventario guardado = inventarioRepository.save(inventario);
        log.info("Inventario creado con id: {}", guardado.getId());
        return guardado;
    }

    public Inventario actualizar(Long id, InventarioDTO dto) {
        log.info("Actualizando inventario con id: {}", id);
        if (dto.getCantidad() != null && dto.getCantidad() < 0) {
            throw new ReglaNegocioException(
                    "La cantidad de stock no puede ser negativa");
        }
        Inventario inventario = buscarPorId(id);
        inventario.setCantidad(dto.getCantidad());
        if (dto.getCantidadMinima() != null) {
            inventario.setCantidadMinima(dto.getCantidadMinima());
        }
        return inventarioRepository.save(inventario);
    }

    public Inventario descontarStock(Long libroId, Integer cantidad) {
        log.info("Descontando {} unidades del libro id: {}", cantidad, libroId);
        if (cantidad == null || cantidad <= 0) {
            throw new ReglaNegocioException(
                    "La cantidad a descontar debe ser mayor a 0");
        }
        Inventario inventario = buscarPorLibroId(libroId);
        if (inventario.getCantidad() < cantidad) {
            log.warn("Stock insuficiente libro id: {} (actual: {}, solicitado: {})",
                    libroId, inventario.getCantidad(), cantidad);
            throw new ReglaNegocioException(
                    "Stock insuficiente para el libro id: " + libroId
                            + " (disponible: " + inventario.getCantidad() + ")");
        }
        inventario.setCantidad(inventario.getCantidad() - cantidad);
        Inventario actualizado = inventarioRepository.save(inventario);
        log.info("Stock actualizado libro id: {} nueva cantidad: {}",
                libroId, actualizado.getCantidad());
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando inventario con id: {}", id);
        if (!inventarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Inventario no encontrado con id: " + id);
        }
        inventarioRepository.deleteById(id);
        log.info("Inventario eliminado con id: {}", id);
    }
}
