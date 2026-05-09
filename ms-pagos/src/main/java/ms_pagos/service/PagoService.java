package ms_pagos.service;

import ms_pagos.dto.PagoDTO;
import ms_pagos.exception.RecursoNoEncontradoException;
import ms_pagos.exception.ReglaNegocioException;
import ms_pagos.model.Pago;
import ms_pagos.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PagoService {

    private static final Logger log =
            LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoClientService pedidoClientService;

    public List<Pago> listarTodos() {
        log.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public List<Pago> listarPorUsuario(Long usuarioId) {
        log.info("Listando pagos del usuario id: {}", usuarioId);
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pago> listarPorPedido(Long pedidoId) {
        log.info("Listando pagos del pedido id: {}", pedidoId);
        return pagoRepository.findByPedidoId(pedidoId);
    }

    public Pago buscarPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        return pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pago no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Pago no encontrado con id: " + id);
                });
    }

    public Pago procesar(PagoDTO dto) {
        log.info("Procesando pago para pedido id: {}", dto.getPedidoId());

        boolean pedidoExiste = pedidoClientService.verificarPedido(dto.getPedidoId());
        if (!pedidoExiste) {
            log.warn("Pedido no encontrado id: {}", dto.getPedidoId());
            throw new ReglaNegocioException(
                    "El pedido no existe: " + dto.getPedidoId());
        }

        Pago pago = new Pago();
        pago.setPedidoId(dto.getPedidoId());
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(Pago.EstadoPago.APROBADO);
        Pago guardado = pagoRepository.save(pago);
        log.info("Pago procesado con id: {} y transaccion: {}",
                guardado.getId(), guardado.getNumeroTransaccion());
        return guardado;
    }

    public Pago actualizarEstado(Long id, String estado) {
        log.info("Actualizando estado del pago id: {} a {}", id, estado);
        Pago pago = buscarPorId(id);
        try {
            pago.setEstado(Pago.EstadoPago.valueOf(estado.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            log.warn("Estado de pago invalido: {}", estado);
            throw new ReglaNegocioException(
                    "Estado de pago invalido: " + estado);
        }
        return pagoRepository.save(pago);
    }

    public void eliminar(Long id) {
        log.info("Eliminando pago con id: {}", id);
        if (!pagoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
        log.info("Pago eliminado con id: {}", id);
    }
}
