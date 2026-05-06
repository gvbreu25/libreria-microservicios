package ms_usuarios.service;

import ms_usuarios.dto.UsuarioDTO;
import ms_usuarios.model.Usuario;
import ms_usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private static final Logger log =
            LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        log.info("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con id: {}", id);
                    return new RuntimeException(
                            "Usuario no encontrado con id: " + id);
                });
    }

    public Usuario crear(UsuarioDTO dto) {
        log.info("Creando usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email ya registrado: {}", dto.getEmail());
            throw new RuntimeException(
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
        return guardado;
    }

    public Usuario actualizar(Long id, UsuarioDTO dto) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = buscarPorId(id);
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        Usuario usuario = buscarPorId(id);
        usuarioRepository.deleteById(usuario.getId());
        log.info("Usuario eliminado con id: {}", id);
    }
}