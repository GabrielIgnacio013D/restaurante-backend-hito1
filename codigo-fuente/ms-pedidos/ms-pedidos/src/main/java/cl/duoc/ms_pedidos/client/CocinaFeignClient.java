package cl.duoc.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

// Le ponemos el nombre del servicio y la URL exacta de tu ms-cocina (puerto 8083)
@FeignClient(name = "ms-cocina", url = "${url.ms-cocina:http://localhost:8083}")
public interface CocinaFeignClient {

    // Apunta exactamente al @PostMapping("/api/menu/pedidos") de la cocina
    @PostMapping("/api/menu/pedidos")
    String enviarPedidoACocina(@RequestBody Map<String, Object> dto);
}