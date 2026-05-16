package cl.duoc.ms_reservas.repository;

import cl.duoc.ms_reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // JpaRepository ya trae los métodos save, findAll, findById y deleteById listos.
}