package cl.duoc.ms_cocina.controller;

import cl.duoc.ms_cocina.model.PlatoCocina;
import cl.duoc.ms_cocina.repository.CocinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocina")
public class CocinaController {

    @Autowired
    private CocinaRepository cocinaRepository;

    @GetMapping
    public List<PlatoCocina> listarTodo() {
        return cocinaRepository.findAll();
    }

    @PostMapping
    public PlatoCocina enviarACocina(@RequestBody PlatoCocina plato) {
        plato.setEstado("EN ESPERA");
        return cocinaRepository.save(plato);
    }
}