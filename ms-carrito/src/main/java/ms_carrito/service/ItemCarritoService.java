package ms_carrito.service;

import ms_carrito.dto.ItemCarritoDTO;
import ms_carrito.exception.RecursoNoEncontradoException;
import ms_carrito.exception.ReglaNegocioException;
import ms_carrito.model.ItemCarrito;
import ms_carrito.repository.ItemCarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemCarritoService {

    private static final Logger log =
            LoggerFactory.getLogger(ItemCarritoService.class);

    @Autowired
    private ItemCarritoRepository carritoRepository;

    @Autowired
    private LibroClientService libroClientService;

    public List<ItemCarrito> listarTodos() {
        log.info("Listando todos los items del carrito");
        return carritoRepository.findAll();
    }

    public List<ItemCarrito> listarPorUsuario(Long usuarioId) {
        log.info("Listando carrito del usuario id: {}", usuarioId);
        return carritoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    public ItemCarrito buscarPorId(Long id) {
        log.info("Buscando item carrito con id: {}", id);
        return carritoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Item no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Item no encontrado con id: " + id);
                });
    }

    public ItemCarrito agregar(ItemCarritoDTO dto) {
        log.info("Agregando libro id: {} al carrito del usuario id: {}",
                dto.getLibroId(), dto.getUsuarioId());

        Double precioReal = libroClientService.obtenerPrecioLibro(dto.getLibroId());
        if (precioReal == null) {
            log.warn("Libro no encontrado en ms-libros id: {}", dto.getLibroId());
            throw new ReglaNegocioException(
                    "El libro no existe o ms-libros no esta disponible: " + dto.getLibroId());
        }

        Optional<ItemCarrito> existente = carritoRepository
                .findByUsuarioIdAndLibroIdAndActivoTrue(dto.getUsuarioId(), dto.getLibroId());

        if (existente.isPresent()) {
            ItemCarrito item = existente.get();
            int nuevaCantidad = item.getCantidad() + dto.getCantidad();
            log.info("Item ya existe id: {}, sumando cantidad ({} + {} = {})",
                    item.getId(), item.getCantidad(), dto.getCantidad(), nuevaCantidad);
            item.setCantidad(nuevaCantidad);
            item.setPrecioUnitario(precioReal);
            return carritoRepository.save(item);
        }

        ItemCarrito item = new ItemCarrito();
        item.setUsuarioId(dto.getUsuarioId());
        item.setLibroId(dto.getLibroId());
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(precioReal);
        ItemCarrito guardado = carritoRepository.save(item);
        log.info("Item agregado al carrito con id: {}", guardado.getId());
        return guardado;
    }

    public ItemCarrito actualizar(Long id, ItemCarritoDTO dto) {
        log.info("Actualizando item carrito con id: {}", id);
        ItemCarrito item = buscarPorId(id);

        Double precioReal = libroClientService.obtenerPrecioLibro(item.getLibroId());
        if (precioReal == null) {
            log.warn("Libro no encontrado en ms-libros id: {}", item.getLibroId());
            throw new ReglaNegocioException(
                    "El libro no existe o ms-libros no esta disponible: " + item.getLibroId());
        }

        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(precioReal);
        return carritoRepository.save(item);
    }

    public void eliminar(Long id) {
        log.info("Eliminando item carrito con id: {}", id);
        if (!carritoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Item no encontrado con id: " + id);
        }
        carritoRepository.deleteById(id);
        log.info("Item eliminado con id: {}", id);
    }

    public void vaciarCarrito(Long usuarioId) {
        log.info("Vaciando carrito del usuario id: {}", usuarioId);
        carritoRepository.deleteByUsuarioId(usuarioId);
        log.info("Carrito vaciado del usuario id: {}", usuarioId);
    }
}
