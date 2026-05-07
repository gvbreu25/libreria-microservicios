package ms_pedidos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class InventarioClientService {

    private static final Logger log =
            LoggerFactory.getLogger(InventarioClientService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    public boolean verificarStock(Long libroId, Integer cantidadRequerida) {
        try {
            log.info("Consultando stock del libro id: {}", libroId);
            Map respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://ms-inventario/api/v1/inventario/libro/" + libroId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (respuesta != null) {
                Integer cantidad = (Integer) respuesta.get("cantidad");
                log.info("Stock disponible: {}", cantidad);
                return cantidad >= cantidadRequerida;
            }
            return false;
        } catch (Exception e) {
            log.error("Error al consultar inventario: {}", e.getMessage());
            return false;
        }
    }
}