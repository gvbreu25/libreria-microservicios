package ms_pagos.service;

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
public class PedidoClientService {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoClientService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Map<String, Object> obtenerPedido(Long pedidoId) {
        try {
            log.info("Consultando pedido id: {}", pedidoId);
            Map<String, Object> respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://ms-pedidos/api/v1/pedidos/" + pedidoId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorResume(ex -> {
                        log.error("Fallo consulta pedido: {}", ex.getMessage());
                        return Mono.empty();
                    })
                    .block();

            if (respuesta != null) {
                log.info("Pedido obtenido id: {} estado: {}",
                        pedidoId, respuesta.get("estado"));
            }
            return respuesta;
        } catch (Exception e) {
            log.error("Error al obtener pedido: {}", e.getMessage());
            return null;
        }
    }

    public boolean verificarPedido(Long pedidoId) {
        return obtenerPedido(pedidoId) != null;
    }

    public String obtenerEstadoPedido(Long pedidoId) {
        Map<String, Object> pedido = obtenerPedido(pedidoId);
        if (pedido == null) return null;
        Object estado = pedido.get("estado");
        return estado != null ? estado.toString() : null;
    }
}
