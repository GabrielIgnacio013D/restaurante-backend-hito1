package cl.duoc.ms_reservas.dto;

public class MesaDTO {
    private Long id;
    private int numero;
    private int capacidad;
    private boolean disponible;

    // CONSTRUCTORES
    public MesaDTO() {}

    public MesaDTO(Long id, int numero, int capacidad, boolean disponible) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.disponible = disponible;
    }

    // GETTERS Y SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}