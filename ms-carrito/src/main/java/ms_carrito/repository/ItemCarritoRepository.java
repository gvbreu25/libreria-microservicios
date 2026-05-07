package ms_carrito.repository;

import ms_carrito.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(Long usuarioId);
    List<ItemCarrito> findByUsuarioIdAndActivoTrue(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
