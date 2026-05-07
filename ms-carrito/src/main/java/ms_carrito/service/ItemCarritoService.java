package ms_carrito.service;

import ms_carrito.dto.ItemCarritoDTO;
import ms_carrito.model.ItemCarrito;
import ms_carrito.repository.ItemCarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ItemCarritoService {

    private static final Logger log =
            LoggerFactory.getLogger(ItemCarritoService.class);

    @Autowired
    private ItemCarritoRepository carritoRepository;

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
                    return new RuntimeException("Item no encontrado con id: " + id);
                });
    }

    public ItemCarrito agregar(ItemCarritoDTO dto) {
        log.info("Agregando libro id: {} al carrito del usuario id: {}",
                dto.getLibroId(), dto.getUsuarioId());
        ItemCarrito item = new ItemCarrito();
        item.setUsuarioId(dto.getUsuarioId());
        item.setLibroId(dto.getLibroId());
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(dto.getPrecioUnitario());
        ItemCarrito guardado = carritoRepository.save(item);
        log.info("Item agregado al carrito con id: {}", guardado.getId());
        return guardado;
    }

    public ItemCarrito actualizar(Long id, ItemCarritoDTO dto) {
        log.info("Actualizando item carrito con id: {}", id);
        ItemCarrito item = buscarPorId(id);
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(dto.getPrecioUnitario());
        return carritoRepository.save(item);
    }

    public void eliminar(Long id) {
        log.info("Eliminando item carrito con id: {}", id);
        ItemCarrito item = buscarPorId(id);
        carritoRepository.deleteById(item.getId());
        log.info("Item eliminado con id: {}", id);
    }

    public void vaciarCarrito(Long usuarioId) {
        log.info("Vaciando carrito del usuario id: {}", usuarioId);
        carritoRepository.deleteByUsuarioId(usuarioId);
        log.info("Carrito vaciado del usuario id: {}", usuarioId);
    }
}