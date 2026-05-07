package ms_pedidos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @PrePersist
    public void prePersist() {
        this.fechaPedido = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.estado == null) this.estado = EstadoPedido.PENDIENTE;
    }

    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO
    }
}