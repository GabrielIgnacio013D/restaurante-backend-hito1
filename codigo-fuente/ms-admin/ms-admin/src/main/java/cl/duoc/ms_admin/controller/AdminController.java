package cl.duoc.ms_admin.controller;
import cl.duoc.ms_admin.model.Configuracion;
import cl.duoc.ms_admin.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ConfigRepository configRepository;

    @GetMapping("/configs")
    public List<Configuracion> listarConfigs() {
        return configRepository.findAll();
    }

    @PostMapping("/configs")
    public Configuracion guardarConfig(@RequestBody Configuracion config) {
        return configRepository.save(config);
    }
}
