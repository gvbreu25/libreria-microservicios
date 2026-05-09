package ms_carrito.repository;

import ms_carrito.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(Long usuarioId);
    List<ItemCarrito> findByUsuarioIdAndActivoTrue(Long usuarioId);
    Optional<ItemCarrito> findByUsuarioIdAndLibroIdAndActivoTrue(Long usuarioId, Long libroId);
    void deleteByUsuarioId(Long usuarioId);
}
