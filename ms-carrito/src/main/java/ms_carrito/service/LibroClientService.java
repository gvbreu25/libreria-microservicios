package ms_carrito.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
            Map<String, Object> respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://ms-libros/api/v1/libros/" + libroId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorResume(ex -> {
                        log.error("Fallo consulta libro: {}", ex.getMessage());
                        return Mono.empty();
                    })
                    .block();

            if (respuesta == null) {
                log.warn("Respuesta vacia desde ms-libros para libro id: {}", libroId);
                return null;
            }
            Object precio = respuesta.get("precio");
            if (precio == null) return null;
            log.info("Precio obtenido: {}", precio);
            return Double.valueOf(precio.toString());
        } catch (Exception e) {
            log.error("Error al consultar libro: {}", e.getMessage());
            return null;
        }
    }
}
