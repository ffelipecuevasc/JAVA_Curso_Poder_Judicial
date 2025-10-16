package org.fcuevas.biblioteca.service.interfaces;

import org.fcuevas.biblioteca.dto.*;
import org.fcuevas.biblioteca.enums.TipoMaterial;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Materiales.
 *
 * Responsabilidad:
 * - Validar entradas (campos obligatorios, rangos, formatos).
 * - Orquestar llamadas al/los DAO(s) correspondientes (supertipo + subtipo).
 * - Aplicar reglas de negocio simples (p.ej., no permitir eliminar si está prestado).
 *
 * Reglas:
 * - El ID (CODIGO / CODIGO_MATERIAL) es de tipo Integer (NUMBER identidad en la BD).
 * - Si la entrada es inválida -> IllegalArgumentException.
 * - Si ocurre un problema de persistencia -> RuntimeException (envolver SQLException).
 */
public interface IMaterialService {

    // ───────────────────────────────
    // CREACIÓN (alta)
    // ───────────────────────────────

    // Crea un LIBRO (inserta MATERIAL + MATERIAL_LIBRO). Devuelve el CODIGO generado para el MATERIAL.
    Integer crearLibro(MaterialDTO base, LibroDTO libro);

    // Crea una REVISTA (inserta MATERIAL + MATERIAL_REVISTA). Devuelve el CODIGO generado para el MATERIAL.
    Integer crearRevista(MaterialDTO base, RevistaDTO revista);

    // Crea un AUDIOLIBRO (inserta MATERIAL + MATERIAL_AUDIOLIBRO). Devuelve el CODIGO generado para el MATERIAL.
    Integer crearAudiolibro(MaterialDTO base, AudioLibroDTO audioLibro);

    // ───────────────────────────────
    // CONSULTA (búsquedas / listados)
    // ───────────────────────────────

    // Busca el MATERIAL por su CODIGO (solo datos del supertipo).
    Optional<MaterialDTO> buscarPorCodigo(Integer codigo);

    // Obtiene el detalle de LIBRO por CODIGO_MATERIAL.
    Optional<LibroDTO> obtenerDetalleLibro(Integer codigoMaterial);

    // Obtiene el detalle de REVISTA por CODIGO_MATERIAL.
    Optional<RevistaDTO> obtenerDetalleRevista(Integer codigoMaterial);

    // Obtiene el detalle de AUDIOLIBRO por CODIGO_MATERIAL.
    Optional<AudioLibroDTO> obtenerDetalleAudioLibro(Integer codigoMaterial);

    // Lista TODOS los materiales (supertipo).
    List<MaterialDTO> listarMateriales();

    // Lista materiales por tipo (LIBRO/REVISTA/AUDIOLIBRO), solo datos del supertipo.
    List<MaterialDTO> listarPorTipo(TipoMaterial tipo);

    // Lista materiales DISPONIBLES por tipo (útil para prestar).
    List<MaterialDTO> listarDisponiblesPorTipo(TipoMaterial tipo);

    // ───────────────────────────────
    // ACTUALIZACIÓN (modificaciones)
    // ───────────────────────────────

    // Actualiza datos del MATERIAL (título, días base, disponible, tipo si aplica). No toca el subtipo.
    boolean actualizarMaterial(MaterialDTO material);

    // Actualiza los datos específicos de LIBRO.
    boolean actualizarDetalleLibro(LibroDTO libro);

    // Actualiza los datos específicos de REVISTA.
    boolean actualizarDetalleRevista(RevistaDTO revista);

    // Actualiza los datos específicos de AUDIOLIBRO.
    boolean actualizarDetalleAudioLibro(AudioLibroDTO audioLibro);

    // Cambia el flag de disponibilidad del material.
    boolean cambiarDisponibilidad(Integer codigoMaterial, boolean disponible);

    // ───────────────────────────────
    // ELIMINACIÓN (baja)
    // ───────────────────────────────

    // Elimina el material por CODIGO.
    boolean eliminarMaterial(Integer codigo);


    // ───────────────────────────────
    // AUXILIARES
    // ───────────────────────────────

    /** ¿Existe un material con ese CODIGO? */
    boolean existeMaterialPorCodigo(Integer codigo);

    /** ¿Existe ya un libro con ese ISBN? (para validar duplicados antes de crear/actualizar). */
    boolean existeLibroPorIsbn(String isbn);

}
