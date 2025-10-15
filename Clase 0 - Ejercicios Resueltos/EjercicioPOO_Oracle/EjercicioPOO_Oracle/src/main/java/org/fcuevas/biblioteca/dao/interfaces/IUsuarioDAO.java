package org.fcuevas.biblioteca.dao.interfaces;

import org.fcuevas.biblioteca.dto.UsuarioDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUsuarioDAO {
    /** Inserta un usuario. Devuelve true si se insertó (1 fila). */
    boolean crear(UsuarioDTO usuario) throws SQLException;

    /** Busca un usuario por su RUT. */
    Optional<UsuarioDTO> buscarPorRut(String rut) throws SQLException;

    /** Lista todos los usuarios ordenados por nombre (criterio libre en la implementación). */
    List<UsuarioDTO> listarTodos() throws SQLException;

    /** Actualiza los datos del usuario identificado por su RUT dentro del propio DTO. */
    boolean actualizar(UsuarioDTO usuario) throws SQLException;

    /** Elimina un usuario por RUT. Devuelve true si eliminó (1 fila). */
    boolean eliminarPorRut(String rut) throws SQLException;

    /* --- Auxiliares útiles para validaciones en Service (opcionales pero recomendados) --- */

    /** Verifica si existe un usuario con ese RUT. */
    boolean existePorRut(String rut) throws SQLException;

    /** Verifica si existe un usuario con ese email. */
    boolean existePorEmail(String email) throws SQLException;
}
