package cl.duoc.ms_admin.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Configuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String parametro; // Ej: "nombre_restaurante"
    private String valor;     // Ej: "La Picá de Docker"
}