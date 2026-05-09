package ms_pedidos.service;

import ms_pedidos.dto.DetallePedidoDTO;
import ms_pedidos.dto.PedidoDTO;
import ms_pedidos.exception.RecursoNoEncontradoException;
import ms_pedidos.exception.ReglaNegocioException;
import ms_pedidos.model.DetallePedido;
import ms_pedidos.model.Pedido;
import ms_pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private InventarioClientService inventarioClientService;

    public List<Pedido> listarTodos() {
        log.info("Listando todos los pedidos");
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorUsuario(Long usuarioId) {
        log.info("Listando pedidos del usuario id: {}", usuarioId);
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> listarPorEstado(Pedido.EstadoPedido estado) {
        log.info("Listando pedidos con estado: {}", estado);
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido buscarPorId(Long id) {
        log.info("Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pedido no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Pedido no encontrado con id: " + id);
                });
    }

    public Pedido crear(PedidoDTO dto) {
        log.info("Creando pedido para usuario id: {}", dto.getUsuarioId());

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setTotal(dto.getTotal());
        pedido.setDireccionEnvio(dto.getDireccionEnvio());

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            for (DetallePedidoDTO d : dto.getDetalles()) {
                verificarStock(d.getLibroId(), d.getCantidad());
                DetallePedido detalle = new DetallePedido();
                detalle.setLibroId(d.getLibroId());
                detalle.setCantidad(d.getCantidad());
                detalle.setPrecioUnitario(d.getPrecioUnitario());
                pedido.agregarDetalle(detalle);
            }
        } else if (dto.getLibroId() != null && dto.getCantidad() != null) {
            verificarStock(dto.getLibroId(), dto.getCantidad());
        }

        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado con id: {} ({} detalles)",
                guardado.getId(), guardado.getDetalles().size());
        return guardado;
    }

    private void verificarStock(Long libroId, Integer cantidad) {
        boolean hayStock = inventarioClientService.verificarStock(libroId, cantidad);
        if (!hayStock) {
            log.warn("Stock insuficiente para libro id: {}", libroId);
            throw new ReglaNegocioException(
                    "Stock insuficiente para el libro id: " + libroId);
        }
    }

    public Pedido actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del pedido id: {} a {}", id, estado);
        Pedido pedido = buscarPorId(id);
        try {
            pedido.setEstado(Pedido.EstadoPedido.valueOf(estado.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            log.warn("Estado de pedido invalido: {}", estado);
            throw new ReglaNegocioException(
                    "Estado de pedido invalido: " + estado);
        }
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pedido con id: {}", id);
        if (!pedidoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
        log.info("Pedido eliminado con id: {}", id);
    }
}
