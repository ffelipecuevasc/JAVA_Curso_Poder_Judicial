package org.fcuevas.biblioteca.dao.interfaces;

import org.fcuevas.biblioteca.dto.AudioLibroDTO;
import org.fcuevas.biblioteca.dto.LibroDTO;
import org.fcuevas.biblioteca.dto.MaterialDTO;
import org.fcuevas.biblioteca.dto.RevistaDTO;
import org.fcuevas.biblioteca.enums.TipoMaterial;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO de Materiales:
 *
 * - El ID (CODIGO) en todas las tablas relacionadas a Material es INTEGER (NUMBER identidad en BD).
 * - Los métodos que "crean" un subtipo deben insertar el MATERIAL base + la fila del subtipo
 *   en una MISMA operación (transacción) y devolver el CODIGO generado.
 * - Las implementaciones deben cerrar todos los recursos JDBC y propagar SQLException.
 */
public interface IMaterialDAO {

    /* ───────────────────────────────
       CREACIÓN (alta)
       ─────────────────────────────── */

    //Crea un LIBRO: inserta MATERIAL (supertipo) + MATERIAL_LIBRO (detalle) en una sola operación.
    Integer crearLibro(MaterialDTO base, LibroDTO libro) throws SQLException;

    // Crea una REVISTA: inserta MATERIAL + MATERIAL_REVISTA en una sola operación.
    Integer crearRevista(MaterialDTO base, RevistaDTO revista) throws SQLException;

    // Crea un AUDIOLIBRO: inserta MATERIAL + MATERIAL_AUDIOLIBRO en una sola operación.
    Integer crearAudiolibro(MaterialDTO base, AudioLibroDTO audioLibro) throws SQLException;

    /* ───────────────────────────────
       CONSULTAS (búsquedas / listados)
       ─────────────────────────────── */

    // Busca el MATERIAL por su CODIGO (solo datos del supertipo).
    Optional<MaterialDTO> buscarPorCodigo(Integer codigo) throws SQLException;

    // Obtiene el detalle de LIBRO (si el material es LIBRO) por CODIGO_MATERIAL.
    Optional<LibroDTO> buscarDetalleLibro(Integer codigoMaterial) throws SQLException;

    // Obtiene el detalle de REVISTA (si el material es REVISTA) por CODIGO_MATERIAL.
    Optional<RevistaDTO> buscarDetalleRevista(Integer codigoMaterial) throws SQLException;

    // Obtiene el detalle de AUDIOLIBRO (si el material es AUDIOLIBRO) por CODIGO_MATERIAL.
    Optional<AudioLibroDTO> buscarDetalleAudioLibro(Integer codigoMaterial) throws SQLException;

    // Lista TODOS los materiales (supertipo).
    List<MaterialDTO> listarTodos() throws SQLException;

    // Lista materiales por tipo (LIBRO / REVISTA / AUDIOLIBRO), solo datos del supertipo.
    List<MaterialDTO> listarPorTipo(TipoMaterial tipo) throws SQLException;

    // Lista materiales DISPONIBLES por tipo (útil para prestar).
    List<MaterialDTO> listarDisponiblesPorTipo(TipoMaterial tipo) throws SQLException;

    /* ───────────────────────────────
       ACTUALIZACIONES (modificaciones)
       ─────────────────────────────── */

    // Actualiza datos del MATERIAL (supertipo): título, días base, disponible, tipo (si procede).
    boolean actualizarMaterial(MaterialDTO material) throws SQLException;

    // Actualiza los datos específicos de LIBRO.
    boolean actualizarDetalleLibro(LibroDTO libro) throws SQLException;

    // Actualiza los datos específicos de REVISTA.
    boolean actualizarDetalleRevista(RevistaDTO revista) throws SQLException;

    // Actualiza los datos específicos de AUDIOLIBRO.
    boolean actualizarDetalleAudioLibro(AudioLibroDTO audioLibro) throws SQLException;

    // Cambia solo el flag de disponibilidad del MATERIAL (S/N en BD).
    boolean cambiarDisponibilidad(Integer codigoMaterial, boolean disponible) throws SQLException;

    /* ───────────────────────────────
       ELIMINACIONES (baja)
       ─────────────────────────────── */

    /* Elimina el material por CODIGO.
    * La implementación debe eliminar primero el detalle del subtipo y luego el MATERIAL.
    *  Deberá fallar si existen préstamos que referencian al material (por FK),
    *  por lo que requerirá validación en la capa SERVICE. */
    boolean eliminarPorCodigo(Integer codigo) throws SQLException;

    /* ───────────────────────────────
       AUXILIARES
       ─────────────────────────────── */

    // ¿Existe un material con ese CODIGO?
    boolean existePorCodigo(Integer codigo) throws SQLException;

    // ¿Existe un material con ese ISBN? (sólo aplica a libros) */
    boolean existeLibroPorIsbn(String isbn) throws SQLException;
}
