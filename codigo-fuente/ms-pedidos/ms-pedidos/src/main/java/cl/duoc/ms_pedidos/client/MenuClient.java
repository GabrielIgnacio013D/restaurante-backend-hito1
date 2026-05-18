package cl.duoc.ms_pedidos.client;

import cl.duoc.ms_pedidos.dto.PlatoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Pongan aquí la IP REAL que te marca AWS en este segundo
@FeignClient(name = "menu-api", url = "34.204.5.186:8081")
public interface MenuClient {

    @GetMapping("/api/menu/{id}")
    PlatoDTO obtenerPlatoPorId(@PathVariable("id") Long id);
}