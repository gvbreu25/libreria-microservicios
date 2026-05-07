package ms_pedidos.service;

import ms_pedidos.dto.PedidoDTO;
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
                    return new RuntimeException("Pedido no encontrado con id: " + id);
                });
    }

    public Pedido crear(PedidoDTO dto) {
        log.info("Creando pedido para usuario id: {}", dto.getUsuarioId());

        // Verificar stock antes de crear el pedido
        if (dto.getLibroId() != null && dto.getCantidad() != null) {
            boolean hayStock = inventarioClientService
                    .verificarStock(dto.getLibroId(), dto.getCantidad());
            if (!hayStock) {
                log.warn("Stock insuficiente para libro id: {}", dto.getLibroId());
                throw new RuntimeException("Stock insuficiente para completar el pedido");
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setTotal(dto.getTotal());
        pedido.setDireccionEnvio(dto.getDireccionEnvio());
        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado con id: {}", guardado.getId());
        return guardado;
    }

    public Pedido actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del pedido id: {} a {}", id, estado);
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(Pedido.EstadoPedido.valueOf(estado.toUpperCase()));
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pedido con id: {}", id);
        Pedido pedido = buscarPorId(id);
        pedidoRepository.deleteById(pedido.getId());
        log.info("Pedido eliminado con id: {}", id);
    }
}