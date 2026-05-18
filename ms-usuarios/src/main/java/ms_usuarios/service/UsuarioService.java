package ms_usuarios.service;

import ms_usuarios.dto.UsuarioDTO;
import ms_usuarios.dto.UsuarioResponseDTO;
import ms_usuarios.exception.RecursoNoEncontradoException;
import ms_usuarios.exception.ReglaNegocioException;
import ms_usuarios.model.Usuario;
import ms_usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    private static final Logger log =
            LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Listando todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException(
                            "Usuario no encontrado con id: " + id);
                });
        return toResponseDTO(usuario);
    }

    public UsuarioResponseDTO crear(UsuarioDTO dto) {
        log.info("Creando usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email ya registrado: {}", dto.getEmail());
            throw new ReglaNegocioException(
                    "El email ya está registrado: " + dto.getEmail());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioDTO dto) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        Usuario actualizado = usuarioRepository.save(usuario);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado con id: {}", id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getRol(),
                u.getActivo(),
                u.getFechaCreacion()
        );
    }
}
