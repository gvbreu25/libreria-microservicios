package ms_pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "numero_transaccion", unique = true)
    private String numeroTransaccion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.estado == null) this.estado = EstadoPago.PENDIENTE;
        this.numeroTransaccion = "TXN-" + System.currentTimeMillis();
    }

    public enum MetodoPago {
        TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, EFECTIVO
    }

    public enum EstadoPago {
        PENDIENTE, APROBADO, RECHAZADO, REEMBOLSADO
    }
}