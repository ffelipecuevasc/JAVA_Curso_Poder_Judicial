package org.fcuevas.biblioteca.dao.interfaces;

import org.fcuevas.biblioteca.dto.PrestamoDTO;
import org.fcuevas.biblioteca.enums.EstadoPrestamo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DAO de Préstamos:
 *
 * - El identificador (CODIGO) de PRESTAMOS es INTEGER (NUMBER identidad en BD).
 * - Las fechas se manejan como LocalDate en DTO/Service; la implementación hará el mapeo JDBC.
 * - La lógica de negocio (cálculo de días de atraso y multa) vive en la capa Service.
 * - Las implementaciones deben cerrar todos los recursos JDBC y propagar SQLException.
 */
public interface IPrestamoDAO {

    /* ───────────────────────────────
       CREACIÓN (alta)
       ─────────────────────────────── */

    // Inserta un préstamo ACTIVO para un material y un usuario.
    Integer crear(PrestamoDTO prestamo) throws SQLException;

    /* ───────────────────────────────
       CONSULTAS
       ─────────────────────────────── */

    // Busca un préstamo por su CODIGO.
    Optional<PrestamoDTO> buscarPorCodigo(Integer codigo) throws SQLException;

    // Lista TODOS los préstamos (activos y devueltos).
    List<PrestamoDTO> listarTodos() throws SQLException;

    // Lista SOLO los préstamos con estado ACTIVO.
    List<PrestamoDTO> listarActivos() throws SQLException;

    // Lista todos los préstamos de un usuario (por RUT).
    List<PrestamoDTO> listarPorUsuario(String rutUsuario) throws SQLException;

    // Lista los préstamos ACTIVOS de un usuario (por RUT).
    List<PrestamoDTO> listarActivosPorUsuario(String rutUsuario) throws SQLException;

    // Lista todos los préstamos que refieren a un material (por CODIGO_MATERIAL).
    List<PrestamoDTO> listarPorMaterial(Integer codigoMaterial) throws SQLException;

    boolean cambiarEstado(Integer codigoPrestamo, EstadoPrestamo estado) throws SQLException;

    /* ───────────────────────────────
       ACTUALIZACIONES
       ─────────────────────────────── */

    // Registra la devolución de un préstamo: establece FECHA_DEVOLUCION, actualiza DIAS_ATRASO
    // y MULTA_APLICADA,cambia ESTADO a DEVUELTO.
    boolean registrarDevolucion(Integer codigoPrestamo, LocalDate fechaDevolucion, int diasAtraso, double multaAplicada) throws SQLException;

    // Actualiza únicamente los montos de control (multa y días de atraso).
    boolean actualizarMontos(Integer codigoPrestamo, int diasAtraso, double multaAplicada) throws SQLException;

    /* ───────────────────────────────
       ELIMINACIONES
       ─────────────────────────────── */

    // Elimina un préstamo por CODIGO.
    boolean eliminarPorCodigo(Integer codigo) throws SQLException;

    /* ───────────────────────────────
       AUXILIARES
       ─────────────────────────────── */

    // ¿Existe un préstamo con ese CODIGO?
    boolean existePorCodigo(Integer codigo) throws SQLException;

    // ¿Existe un préstamo ACTIVO que refiera a este material? (bloquea prestar de nuevo el mismo material)
    boolean existePrestamoActivoPorMaterial(Integer codigoMaterial) throws SQLException;

    // Cuenta préstamos ACTIVOS del usuario (para validar cupo antes de prestar).
    long contarPrestamosActivosPorUsuario(String rutUsuario) throws SQLException;
}
