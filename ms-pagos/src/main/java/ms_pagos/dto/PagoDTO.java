package ms_pagos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ms_pagos.model.Pago;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "El método de pago es obligatorio")
    private Pago.MetodoPago metodoPago;
}