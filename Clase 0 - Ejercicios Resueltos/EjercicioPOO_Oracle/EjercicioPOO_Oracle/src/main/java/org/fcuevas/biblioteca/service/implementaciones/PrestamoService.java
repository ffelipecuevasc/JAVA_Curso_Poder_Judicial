package org.fcuevas.biblioteca.service.implementaciones;

import org.fcuevas.biblioteca.dao.implementaciones.MaterialDAO;
import org.fcuevas.biblioteca.dao.implementaciones.PrestamoDAO;
import org.fcuevas.biblioteca.dao.implementaciones.UsuarioDAO;
import org.fcuevas.biblioteca.dao.interfaces.IMaterialDAO;
import org.fcuevas.biblioteca.dao.interfaces.IPrestamoDAO;
import org.fcuevas.biblioteca.dao.interfaces.IUsuarioDAO;
import org.fcuevas.biblioteca.dto.MaterialDTO;
import org.fcuevas.biblioteca.dto.PrestamoDTO;
import org.fcuevas.biblioteca.dto.UsuarioDTO;
import org.fcuevas.biblioteca.enums.*;
import org.fcuevas.biblioteca.service.interfaces.IPrestamoService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class PrestamoService implements IPrestamoService {

    // Se establece una multa única por día
    private static final double MULTA_POR_DIA = 100.0;

    private final IPrestamoDAO prestamoDAO;
    private final IMaterialDAO materialDAO;
    private final IUsuarioDAO usuarioDAO;

    // Constructor por defecto: usa implementaciones JDBC.
    public PrestamoService() {
        this(new PrestamoDAO(), new MaterialDAO(), new UsuarioDAO());
    }

    // Constructor inyectando dependencias (útil para tests o cambiar implementaciones).
    public PrestamoService(IPrestamoDAO prestamoDAO, IMaterialDAO materialDAO, IUsuarioDAO usuarioDAO) {
        this.prestamoDAO = prestamoDAO;
        this.materialDAO = materialDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Integer crearPrestamo(PrestamoDTO prestamo) {
        validarNuevoPrestamo(prestamo);

        try {
            // 1) Material debe existir y estar disponible
            MaterialDTO material = materialDAO.buscarPorCodigo(prestamo.getCodigoMaterial())
                    .orElseThrow(() -> new IllegalArgumentException("El material no existe"));
            if (!material.isDisponible())
                throw new IllegalArgumentException("El material no está disponible");

            // 2) Usuario debe existir y no exceder cupo
            UsuarioDTO usuario = usuarioDAO.buscarPorRut(prestamo.getRutUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
            long activosDelUsuario = prestamoDAO.contarPrestamosActivosPorUsuario(usuario.getRut());
            if (activosDelUsuario >= usuario.getCupoMax())
                throw new IllegalArgumentException("El usuario alcanzó su cupo de préstamos activos");

            // 3) Asegurar que no exista préstamo ACTIVO para el mismo material (doble control)
            if (prestamoDAO.existePrestamoActivoPorMaterial(material.getCodigo()))
                throw new IllegalArgumentException("Ya existe un préstamo ACTIVO para este material");

            // 4) Crear préstamo
            prestamo.setEstado(EstadoPrestamo.ACTIVO);
            if (prestamo.getFechaPrestamo() == null) prestamo.setFechaPrestamo(LocalDate.now());
            prestamo.setMultaAplicada(0.0);
            prestamo.setDiasAtraso(0);

            Integer idGenerado = prestamoDAO.crear(prestamo);

            // 5) Marcar material como no disponible y aumentar cupoActual del usuario
            boolean okDisp = materialDAO.cambiarDisponibilidad(material.getCodigo(), false);
            if (!okDisp) {
                // Compensación: borrar préstamo creado
                prestamoDAO.eliminarPorCodigo(idGenerado);
                throw new RuntimeException("No se pudo cambiar la disponibilidad del material");
            }

            // Ajuste de cupoActual (si usas ese campo como contador en BD)
            usuario.setCupoActual(usuario.getCupoActual() + 1);
            boolean okCupo = usuarioDAO.actualizar(usuario);
            if (!okCupo) {
                // Compensación: revertir cambios
                materialDAO.cambiarDisponibilidad(material.getCodigo(), true);
                prestamoDAO.eliminarPorCodigo(idGenerado);
                throw new RuntimeException("No se pudo actualizar el cupo del usuario");
            }

            return idGenerado;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear préstamo: " + e.getMessage());
        }
    }

    @Override
    public Optional<PrestamoDTO> buscarPorCodigo(Integer codigo) {
        validarId(codigo);
        try {
            return prestamoDAO.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamo por código: " + e.getMessage());
        }
    }

    @Override
    public List<PrestamoDTO> listarPrestamos() {
        try {
            return prestamoDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos: " + e.getMessage());
        }
    }

    @Override
    public List<PrestamoDTO> listarPrestamosActivos() {
        try {
            return prestamoDAO.listarActivos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos activos: " + e.getMessage());
        }
    }

    @Override
    public List<PrestamoDTO> listarPrestamosPorUsuario(String rutUsuario) {
        validarRut(rutUsuario);
        try {
            return prestamoDAO.listarPorUsuario(rutUsuario.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos por usuario: " + e.getMessage());
        }
    }

    @Override
    public List<PrestamoDTO> listarPrestamosActivosPorUsuario(String rutUsuario) {
        validarRut(rutUsuario);
        try {
            return prestamoDAO.listarActivosPorUsuario(rutUsuario.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos activos por usuario: " + e.getMessage());
        }
    }

    @Override
    public List<PrestamoDTO> listarPrestamosPorMaterial(Integer codigoMaterial) {
        validarId(codigoMaterial);
        try {
            return prestamoDAO.listarPorMaterial(codigoMaterial);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar préstamos por material: " + e.getMessage());
        }
    }

    @Override
    public boolean registrarDevolucion(Integer codigoPrestamo, LocalDate fechaDevolucion) {
        validarId(codigoPrestamo);
        if (fechaDevolucion == null) fechaDevolucion = LocalDate.now();

        try {
            PrestamoDTO p = prestamoDAO.buscarPorCodigo(codigoPrestamo)
                    .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo"));

            if (p.getEstado() == EstadoPrestamo.DEVUELTO)
                throw new IllegalArgumentException("El préstamo ya fue devuelto");

            // Calcular atraso/multa según días base del material
            MaterialDTO material = materialDAO.buscarPorCodigo(p.getCodigoMaterial())
                    .orElseThrow(() -> new IllegalArgumentException("El material del préstamo no existe"));

            int diasTomados = (int) ChronoUnit.DAYS.between(p.getFechaPrestamo(), fechaDevolucion);
            int diasAtraso = Math.max(0, diasTomados - material.getDiasBase());
            double multa = diasAtraso * MULTA_POR_DIA;

            boolean ok = prestamoDAO.registrarDevolucion(codigoPrestamo, fechaDevolucion, diasAtraso, multa);
            if (!ok) return false;

            // Marcar material como disponible
            materialDAO.cambiarDisponibilidad(material.getCodigo(), true);

            // Disminuir cupoActual del usuario (si usas ese contador)
            UsuarioDTO usuario = usuarioDAO.buscarPorRut(p.getRutUsuario())
                    .orElse(null);
            if (usuario != null) {
                usuario.setCupoActual(Math.max(0, usuario.getCupoActual() - 1));
                usuarioDAO.actualizar(usuario);
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar devolución: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstado(Integer codigoPrestamo, EstadoPrestamo estado) {
        validarId(codigoPrestamo);
        if (estado == null) throw new IllegalArgumentException("El estado es obligatorio");
        try {
            return prestamoDAO.cambiarEstado(codigoPrestamo, estado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado del préstamo: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarMontos(Integer codigoPrestamo, int diasAtraso, double multaAplicada) {
        validarId(codigoPrestamo);
        if (diasAtraso < 0) throw new IllegalArgumentException("Los días de atraso no pueden ser negativos");
        if (multaAplicada < 0) throw new IllegalArgumentException("La multa no puede ser negativa");
        try {
            return prestamoDAO.actualizarMontos(codigoPrestamo, diasAtraso, multaAplicada);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar montos del préstamo: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarPrestamo(Integer codigo) {
        validarId(codigo);
        try {
            return prestamoDAO.eliminarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar préstamo: " + e.getMessage());
        }
    }

    @Override
    public boolean existePrestamoActivoPorMaterial(Integer codigoMaterial) {
        validarId(codigoMaterial);
        try {
            return prestamoDAO.existePrestamoActivoPorMaterial(codigoMaterial);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar préstamos activos por material: " + e.getMessage());
        }
    }

    @Override
    public long contarPrestamosActivosPorUsuario(String rutUsuario) {
        validarRut(rutUsuario);
        try {
            return prestamoDAO.contarPrestamosActivosPorUsuario(rutUsuario.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar préstamos activos del usuario: " + e.getMessage());
        }
    }

    // ───────────────────────────────
    // Validaciones básicas
    // ───────────────────────────────
    private static void validarNuevoPrestamo(PrestamoDTO p) {
        if (p == null) throw new IllegalArgumentException("El préstamo es obligatorio");
        validarId(p.getCodigoMaterial());
        validarRut(p.getRutUsuario());
        if (p.getMultaAplicada() < 0) throw new IllegalArgumentException("La multa no puede ser negativa");
        if (p.getDiasAtraso() < 0) throw new IllegalArgumentException("Los días de atraso no pueden ser negativos");
    }

    private static void validarId(Integer id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El código debe ser un entero positivo");
    }

    private static void validarRut(String rut) {
        if (rut == null || rut.trim().isEmpty())
            throw new IllegalArgumentException("El RUT es obligatorio");
        if (rut.length() > 15)
            throw new IllegalArgumentException("El RUT excede el largo máximo (15)");
    }
}
