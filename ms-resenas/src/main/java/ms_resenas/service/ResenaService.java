package ms_resenas.service;

import ms_resenas.dto.ResenaDTO;
import ms_resenas.exception.RecursoNoEncontradoException;
import ms_resenas.exception.ReglaNegocioException;
import ms_resenas.model.Resena;
import ms_resenas.repository.ResenaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ResenaService {

    private static final Logger log =
            LoggerFactory.getLogger(ResenaService.class);

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> listarTodos() {
        log.info("Listando todas las resenas");
        return resenaRepository.findAll();
    }

    public List<Resena> listarPorLibro(Long libroId) {
        log.info("Listando resenas del libro id: {}", libroId);
        return resenaRepository.findByLibroId(libroId);
    }

    public List<Resena> listarPorUsuario(Long usuarioId) {
        log.info("Listando resenas del usuario id: {}", usuarioId);
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public Resena buscarPorId(Long id) {
        log.info("Buscando resena con id: {}", id);
        return resenaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Resena no encontrada con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Resena no encontrada con id: " + id);
                });
    }

    public Resena crear(ResenaDTO dto) {
        log.info("Creando resena del usuario id: {} para libro id: {}",
                dto.getUsuarioId(), dto.getLibroId());
        validarCalificacion(dto.getCalificacion());
        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setLibroId(dto.getLibroId());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        Resena guardada = resenaRepository.save(resena);
        log.info("Resena creada con id: {}", guardada.getId());
        return guardada;
    }

    public Resena actualizar(Long id, ResenaDTO dto) {
        log.info("Actualizando resena con id: {}", id);
        validarCalificacion(dto.getCalificacion());
        Resena resena = buscarPorId(id);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {
        log.info("Eliminando resena con id: {}", id);
        if (!resenaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Resena no encontrada con id: " + id);
        }
        resenaRepository.deleteById(id);
        log.info("Resena eliminada con id: {}", id);
    }

    private void validarCalificacion(Integer calificacion) {
        if (calificacion == null || calificacion < 1 || calificacion > 5) {
            log.warn("Calificacion fuera de rango: {}", calificacion);
            throw new ReglaNegocioException(
                    "La calificacion debe estar entre 1 y 5");
        }
    }
}
