package ms_libros.service;

import ms_libros.dto.LibroDTO;
import ms_libros.exception.RecursoNoEncontradoException;
import ms_libros.exception.ReglaNegocioException;
import ms_libros.model.Libro;
import ms_libros.repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LibroService {

    private static final Logger log =
            LoggerFactory.getLogger(LibroService.class);

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarTodos() {
        log.info("Listando todos los libros");
        return libroRepository.findAll();
    }

    public List<Libro> listarActivos() {
        log.info("Listando libros activos");
        return libroRepository.findByActivoTrue();
    }

    public Libro buscarPorId(Long id) {
        log.info("Buscando libro con id: {}", id);
        return libroRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Libro no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Libro no encontrado con id: " + id);
                });
    }

    public Libro crear(LibroDTO dto) {
        log.info("Creando libro con ISBN: {}", dto.getIsbn());
        if (libroRepository.existsByIsbn(dto.getIsbn())) {
            log.warn("ISBN ya registrado: {}", dto.getIsbn());
            throw new ReglaNegocioException(
                    "El ISBN ya está registrado: " + dto.getIsbn());
        }
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setIsbn(dto.getIsbn());
        libro.setPrecio(dto.getPrecio());
        libro.setDescripcion(dto.getDescripcion());
        libro.setCategoriaId(dto.getCategoriaId());
        Libro guardado = libroRepository.save(libro);
        log.info("Libro creado con id: {}", guardado.getId());
        return guardado;
    }

    public Libro actualizar(Long id, LibroDTO dto) {
        log.info("Actualizando libro con id: {}", id);
        Libro libro = buscarPorId(id);
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setIsbn(dto.getIsbn());
        libro.setPrecio(dto.getPrecio());
        libro.setDescripcion(dto.getDescripcion());
        libro.setCategoriaId(dto.getCategoriaId());
        return libroRepository.save(libro);
    }

    public void eliminar(Long id) {
        log.info("Eliminando libro con id: {}", id);
        if (!libroRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
        log.info("Libro eliminado con id: {}", id);
    }
}
