package org.fcuevas.biblioteca.service.interfaces;

import org.fcuevas.biblioteca.dto.UsuarioDTO;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    /** Crea un usuario nuevo (valida RUT, email, cupos, duplicados). */
    boolean crearUsuario(UsuarioDTO usuario);

    /** Devuelve un usuario por RUT si existe. */
    Optional<UsuarioDTO> buscarPorRut(String rut);

    /** Lista todos los usuarios (orden a definir por la implementación). */
    List<UsuarioDTO> listarUsuarios();

    /** Actualiza datos del usuario (nombre, email, cupos). */
    boolean actualizarUsuario(UsuarioDTO usuario);

    /** Elimina un usuario por RUT. */
    boolean eliminarUsuario(String rut);

    /* --- Auxiliares útiles para el menú --- */

    /** ¿Existe un usuario con ese RUT? */
    boolean existeUsuarioPorRut(String rut);

    /** ¿Existe un usuario con ese email? */
    boolean existeUsuarioPorEmail(String email);
}
