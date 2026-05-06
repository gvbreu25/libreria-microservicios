package ms_libros.repository;

import ms_libros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByIsbn(String isbn);
    Boolean existsByIsbn(String isbn);
    List<Libro> findByActivoTrue();
    List<Libro> findByCategoriaId(Long categoriaId);
}