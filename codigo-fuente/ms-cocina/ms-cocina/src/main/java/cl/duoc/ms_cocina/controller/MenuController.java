package cl.duoc.ms_cocina.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    // Este es el endpoint que va a escuchar a tu otro microservicio
    @PostMapping("/pedidos") 
    public ResponseEntity<String> recibirPedidoDeMiCompañero(@RequestBody Map<String, Object> dto) {
        System.out.println("=========================================");
        System.out.println("¡ALERTA: LLEGÓ UN PEDIDO DESDE OTRO MICROSERVICIO!");
        
        System.out.println("Plato solicitado: " + dto.getOrDefault("plato", "No especificado"));
        System.out.println("Precio: $" + dto.getOrDefault("precio", "0"));
        System.out.println("Mesa asignada: " + dto.getOrDefault("mesa", "No asignada"));
        System.out.println("=========================================");

        return ResponseEntity.ok("Pedido recibido con éxito en la cocina.");
    }
}