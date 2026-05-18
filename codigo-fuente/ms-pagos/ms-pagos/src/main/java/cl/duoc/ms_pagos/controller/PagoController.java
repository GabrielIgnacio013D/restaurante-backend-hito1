package cl.duoc.ms_pagos.controller;

import cl.duoc.ms_pagos.client.AdminFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private AdminFeignClient adminFeignClient; // <- Conectamos el teléfono aquí

    @PostMapping("/procesar")
    public ResponseEntity<String> recibirPago(@RequestBody Map<String, Object> datosPago) {
        // 1. Imprime la alerta en la terminal de pagos
        System.out.println("=========================================");
        System.out.println("¡ALERTA: PROCESANDO PAGO DESDE PEDIDOS!");
        System.out.println("Total a Pagar: $" + datosPago.getOrDefault("precio", "0"));
        System.out.println("Detalle del Plato: " + datosPago.getOrDefault("plato", "No especificado"));
        System.out.println("Mesa: " + datosPago.getOrDefault("mesa", "No asignada"));
        System.out.println("=========================================");

        // 2. ¡Aquí usamos el teléfono! Pagos llama automáticamente a Administración
        System.out.println("[PAGOS] -> Enviando reporte de recaudación a ms-admin...");
        String respuestaAdmin = adminFeignClient.informarIngresoAAdmin(datosPago); 

        // 3. Le responde de vuelta a pedidos confirmando todo
        return ResponseEntity.ok("Pago procesado en ms-pagos con éxito. Reporte: " + respuestaAdmin);
    }
}