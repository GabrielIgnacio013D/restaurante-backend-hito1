package cl.duoc.ms_pedidos.dto;

import lombok.Data;

@Data
public class PlatoCocinaDTO {
    private Long pedidoId;
    private String nombrePlato;
    private String notasEspeciales;
    private String estado; // Este lo recibirá Cocina
    private Integer mesa;
}