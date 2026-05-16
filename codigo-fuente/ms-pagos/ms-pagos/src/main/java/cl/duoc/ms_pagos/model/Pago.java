package cl.duoc.ms_pagos.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long pedidoId;     // Para saber qué pedido estamos pagando
    private Double monto;
    private String metodoPago; // "EFECTIVO" o "TARJETA"
    private LocalDateTime fecha = LocalDateTime.now();
}