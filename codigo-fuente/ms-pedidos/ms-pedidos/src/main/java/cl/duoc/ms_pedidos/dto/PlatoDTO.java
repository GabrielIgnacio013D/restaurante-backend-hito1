package cl.duoc.ms_pedidos.dto;

import lombok.Data;

@Data
public class PlatoDTO {
    private Long id;
    private String nombre; // 👈 Cambiado de nombrePlato a nombre para activar el getNombre()
    private Double precio;
}