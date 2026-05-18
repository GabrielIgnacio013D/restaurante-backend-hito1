package cl.duoc.ms_reservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// NOTA: Si aún no crean el DTO en reservas, pueden importar Object temporalmente para probar la conexión:
// import java.lang.Object; 

@FeignClient(name = "mesas-api", url = "http://34.204.5.186:8083")
public interface MesaClient {

    // Si da error el DTO, usen Object temporalmente solo para validar que el puente a AWS responda
    @GetMapping("/api/mesas/{id}") 
    Object obtenerMesaPorId(@PathVariable("id") Long id);
}
