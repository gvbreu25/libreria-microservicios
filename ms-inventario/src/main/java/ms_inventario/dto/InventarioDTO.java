package ms_inventario.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @Min(value = 0, message = "La cantidad mínima no puede ser negativa")
    private Integer cantidadMinima;
}