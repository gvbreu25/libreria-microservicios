package ms_carrito.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class LibroClientService {

    private static final Logger log =
            LoggerFactory.getLogger(LibroClientService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Double obtenerPrecioLibro(Long libroId) {
        try {
            log.info("Consultando precio del libro id: {}", libroId);
            Map respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://ms-libros/api/v1/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (respuesta != null) {
                Object precio = respuesta.get("precio");
                log.info("Precio obtenido: {}", precio);
                return Double.valueOf(precio.toString());
            }
            return null;
        } catch (Exception e) {
            log.error("Error al consultar libro: {}", e.getMessage());
            return null;
        }
    }
}