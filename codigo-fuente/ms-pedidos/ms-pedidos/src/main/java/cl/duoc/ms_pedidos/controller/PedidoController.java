package cl.duoc.ms_pedidos.controller;

import cl.duoc.ms_pedidos.client.CocinaFeignClient;
import cl.duoc.ms_pedidos.client.PagoFeignClient;
import cl.duoc.ms_pedidos.client.MenuClient; // 👈 Importamos su cliente de Menú (el que te apunta a ti)
import cl.duoc.ms_pedidos.dto.PlatoDTO;     // 👈 Importamos el DTO de los platos
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private CocinaFeignClient cocinaFeignClient;

    @Autowired
    private PagoFeignClient pagoFeignClient;

    @Autowired
    private MenuClient menuClient; // 👈 Inyectamos el puente hacia el menú de Matías

    // ==========================================
    // 🔥 EL NUEVO ENDPOINT PARA LA PRUEBA CON MATÍAS
    // ==========================================
    @GetMapping("/traer-plato/{id}")
    public ResponseEntity<PlatoDTO> obtenerPlatoDeMatias(@PathVariable("id") Long id) {
        System.out.println("[PEDIDOS] -> Viajando a AWS de Matías a buscar el plato ID: " + id);
        
        // Llama a tu AWS usando Feign
        PlatoDTO plato = menuClient.obtenerPlatoPorId(id);
        
        System.out.println("[PEDIDOS] -> ¡Éxito! Matías me devolvió el plato: " + plato.getNombre());
        return ResponseEntity.ok(plato);
    }

    @PostMapping("/probar-conexion")
    public ResponseEntity<String> simularEnvioACocina() {
        Map<String, Object> datosPedido = new HashMap<>();
        datosPedido.put("plato", "Pastel de Choclo (Desde MS Pedidos)");
        datosPedido.put("precio", "9500");
        datosPedido.put("mesa", "Mesa 7");

        System.out.println("[PEDIDOS] -> Enviando datos a ms-cocina...");
        String respuestaCocina = cocinaFeignClient.enviarPedidoACocina(datosPedido);

        System.out.println("[PEDIDOS] -> Enviando datos a ms-pagos...");
        String respuestaPagos = pagoFeignClient.enviarPagoAPagos(datosPedido);

        return ResponseEntity.ok("Simulación completada:\n1) " + respuestaCocina + "\n2) " + respuestaPagos);
    }
}