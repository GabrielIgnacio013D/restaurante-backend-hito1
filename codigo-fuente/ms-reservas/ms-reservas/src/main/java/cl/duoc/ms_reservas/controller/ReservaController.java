package cl.duoc.ms_reservas.controller;

import cl.duoc.ms_reservas.model.Reserva;
import cl.duoc.ms_reservas.repository.ReservaRepository;
import cl.duoc.ms_reservas.client.MesaClient; // 👈 1. IMPORTANTE: Importar el cliente Feign
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired // 👈 2. Inyectar el MesaClient que se conecta a tu AWS
    private MesaClient mesaClient;

    // LEER TODOS
    @GetMapping
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // CREAR (Modificado para probar el puente a AWS)
    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        // 👈 3. Bloque de prueba de comunicación síncrona
        try {
            System.out.println("[RESERVAS] -> Solicitando verificación de mesa en AWS...");
            
            // Asumiendo que tu entidad Reserva tiene un método para obtener el ID de la mesa (ej: reserva.getIdMesa())
            // Usamos 1L de prueba fija por si acaso tu entidad maneja el campo con otro nombre
            Object mesaDesdeAWS = mesaClient.obtenerMesaPorId(1L); 
            
            System.out.println("[RESERVAS] -> ¡Conexión Exitosa! Datos de la mesa recibidos de AWS: " + mesaDesdeAWS);
        } catch (Exception e) {
            System.out.println("[RESERVAS] -> Error al conectar con el AWS de Matías: " + e.getMessage());
        }

        // Sigue con su flujo normal guardando en su base de datos local
        return reservaRepository.save(reserva);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Reserva obtenerPorId(@PathVariable Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminarReserva(@PathVariable Long id) {
        reservaRepository.deleteById(id);
    }
    }
    
    