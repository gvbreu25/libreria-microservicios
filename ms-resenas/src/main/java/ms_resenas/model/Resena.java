package ms_resenas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "libro_id", nullable = false)
    private Long libroId;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(length = 500)
    private String comentario;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_resena")
    private LocalDateTime fechaResena;

    @PrePersist
    public void prePersist() {
        this.fechaResena = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
    }
}