package cl.duoc.ms_admin.repository;

import cl.duoc.ms_admin.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Configuracion, Long> {
}