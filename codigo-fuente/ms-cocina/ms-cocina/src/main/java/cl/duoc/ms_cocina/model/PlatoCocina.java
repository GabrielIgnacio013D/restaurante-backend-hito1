package cl.duoc.ms_cocina.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PlatoCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long pedidoId; // Referencia al pedido
    private String nombrePlato;
    private String notasEspeciales; // Ej: "Sin cebolla"
    private String estado; // "EN ESPERA", "PREPARANDO", "LISTO"
}
