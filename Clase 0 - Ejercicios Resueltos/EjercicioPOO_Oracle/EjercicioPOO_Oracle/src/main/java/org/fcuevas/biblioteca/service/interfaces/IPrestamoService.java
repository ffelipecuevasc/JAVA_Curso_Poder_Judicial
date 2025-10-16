package org.fcuevas.biblioteca.service.interfaces;

import org.fcuevas.biblioteca.dto.PrestamoDTO;
import org.fcuevas.biblioteca.enums.EstadoPrestamo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de Préstamos.
 *
 * Responsabilidad:
 * - Validar entradas (material/usuario/fechas).
 * - Aplicar reglas de negocio (cupo del usuario, disponibilidad del material, cálculo de atraso/multa).
 * - Orquestar el DAO de préstamos (y coordinar con MaterialService para disponibilidad).
 *
 * Reglas:
 * - IDs (CODIGO y CODIGO_MATERIAL) son Integer (IDENTITY en la BD).
 * - Si la entrada es inválida -> IllegalArgumentException.
 * - Si ocurre un problema de persistencia -> RuntimeException.
 */
public interface IPrestamoService {

    // ───────────────────────────────
    // CREACIÓN
    // ───────────────────────────────

    // Crea un préstamo ACTIVO para un material y usuario.
    // Debe validar:que el material exista y esté disponible,que el usuario exista y no exceda
    // su cupo de préstamos activos
    Integer crearPrestamo(PrestamoDTO prestamo);

    // ───────────────────────────────
    // CONSULTAS
    // ───────────────────────────────

    // Busca un préstamo por su CODIGO.
    Optional<PrestamoDTO> buscarPorCodigo(Integer codigo);

    // Lista TODOS los préstamos.
    List<PrestamoDTO> listarPrestamos();

    // Lista SOLO los préstamos ACTIVOS.
    List<PrestamoDTO> listarPrestamosActivos();

    // Lista todos los préstamos (activos o no) de un usuario.
    List<PrestamoDTO> listarPrestamosPorUsuario(String rutUsuario);

    // Lista los préstamos ACTIVOS de un usuario.
    List<PrestamoDTO> listarPrestamosActivosPorUsuario(String rutUsuario);

    // Lista todos los préstamos que refieren a un material.
    List<PrestamoDTO> listarPrestamosPorMaterial(Integer codigoMaterial);

    // ───────────────────────────────
    // ACTUALIZACIONES
    // ───────────────────────────────

    // Registra la devolución.
    boolean registrarDevolucion(Integer codigoPrestamo, LocalDate fechaDevolucion);

    // Cambia el estado de un préstamo (uso excepcional; normalmente se usa registrarDevolucion).
    boolean cambiarEstado(Integer codigoPrestamo, EstadoPrestamo estado);

    // Actualiza solo DIAS_ATRASO y MULTA_APLICADA (ajuste administrativo).
    boolean actualizarMontos(Integer codigoPrestamo, int diasAtraso, double multaAplicada);

    // ───────────────────────────────
    // ELIMINACIÓN
    // ───────────────────────────────

    // Elimina un préstamo por CODIGO. Uso acotado a correcciones.
    boolean eliminarPrestamo(Integer codigo);

    // ───────────────────────────────
    // AUXILIARES PARA OTRAS CAPAS
    // ───────────────────────────────

    // ¿Existe un préstamo ACTIVO que refiera a este material?
    boolean existePrestamoActivoPorMaterial(Integer codigoMaterial);

    // ¿Cuántos préstamos ACTIVOS tiene un usuario? (para validar cupos).
    long contarPrestamosActivosPorUsuario(String rutUsuario);
}
