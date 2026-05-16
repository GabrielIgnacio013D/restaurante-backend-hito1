package cl.duoc.ms_pagos.controller;

import cl.duoc.ms_pagos.model.Pago;
import cl.duoc.ms_pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping
    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    @PostMapping
    public Pago crear(@RequestBody Pago pago) {
        return pagoRepository.save(pago);
    }
}