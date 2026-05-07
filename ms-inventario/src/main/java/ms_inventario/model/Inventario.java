package ms_inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libro_id", nullable = false, unique = true)
    private Long libroId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "cantidad_minima", nullable = false)
    private Integer cantidadMinima = 5;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.cantidadMinima == null) this.cantidadMinima = 5;
    }
}