package cl.duoc.ms_cocina.repository;

import cl.duoc.ms_cocina.model.PlatoCocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocinaRepository extends JpaRepository<PlatoCocina, Long> {
}