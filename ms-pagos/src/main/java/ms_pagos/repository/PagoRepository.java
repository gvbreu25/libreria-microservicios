package ms_pagos.repository;

import ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
    List<Pago> findByPedidoId(Long pedidoId);
    List<Pago> findByEstado(Pago.EstadoPago estado);
    Optional<Pago> findByNumeroTransaccion(String numeroTransaccion);
    boolean existsByPedidoIdAndEstado(Long pedidoId, Pago.EstadoPago estado);
}
