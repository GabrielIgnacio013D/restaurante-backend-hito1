package cl.duoc.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "ms-pagos", url = "${url.ms-pagos:http://localhost:8084}")
public interface PagoFeignClient {

    @PostMapping("/api/pagos/procesar")
    String enviarPagoAPagos(@RequestBody Map<String, Object> dto);
}