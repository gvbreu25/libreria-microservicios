package ms_pagos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class PedidoClientService {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoClientService.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    public boolean verificarPedido(Long pedidoId) {
        try {
            log.info("Verificando pedido id: {}", pedidoId);
            Map respuesta = webClientBuilder.build()
                    .get()
                    .uri("http://ms-pedidos/api/v1/pedidos/" + pedidoId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (respuesta != null) {
                log.info("Pedido verificado: {}", pedidoId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error al verificar pedido: {}", e.getMessage());
            return false;
        }
    }
}