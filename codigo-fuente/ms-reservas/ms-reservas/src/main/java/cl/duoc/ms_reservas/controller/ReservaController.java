package cl.duoc.ms_reservas.controller;

import cl.duoc.ms_reservas.model.Reserva;
import cl.duoc.ms_reservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    // LEER TODOS
    @GetMapping
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // CREAR
    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
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