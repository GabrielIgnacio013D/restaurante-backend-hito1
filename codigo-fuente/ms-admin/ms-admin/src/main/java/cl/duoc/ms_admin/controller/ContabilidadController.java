package cl.duoc.ms_admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class ContabilidadController {

    @PostMapping("/consolidar-ingreso")
    public ResponseEntity<String> registrarIngreso(@RequestBody Map<String, Object> datosTransaccion) {
        System.out.println("=========================================");
        System.out.println("¡ALERTA ADMIN: NUEVO INGRESO REGISTRADO EN LA CAJA!");
        System.out.println("Monto Ingresado: $" + datosTransaccion.getOrDefault("precio", "0"));
        System.out.println("Glosa: Cuenta pagada de la " + datosTransaccion.getOrDefault("mesa", "Desconocida"));
        System.out.println("=========================================");

        return ResponseEntity.ok("Ingreso asentado en los libros contables de ms-admin.");
    }
}