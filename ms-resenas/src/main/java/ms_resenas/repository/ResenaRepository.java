package ms_resenas.repository;

import ms_resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByLibroId(Long libroId);
    List<Resena> findByUsuarioId(Long usuarioId);
    List<Resena> findByActivoTrue();
    List<Resena> findByCalificacion(Integer calificacion);
}