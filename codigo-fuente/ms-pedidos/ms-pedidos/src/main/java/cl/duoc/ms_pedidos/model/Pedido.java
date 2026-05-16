package cl.duoc.ms_pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String plato;
    private Double precio;
    private Integer mesa;
    private String estado; // Ejemplo: "PENDIENTE", "EN PREPARACION", "ENTREGADO"
}