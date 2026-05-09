package ms_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ms_usuarios.model.Usuario;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Usuario.Rol rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
