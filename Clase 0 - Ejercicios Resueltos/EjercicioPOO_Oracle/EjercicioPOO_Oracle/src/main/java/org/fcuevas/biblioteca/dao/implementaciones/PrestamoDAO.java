package org.fcuevas.biblioteca.dao.implementaciones;

import org.fcuevas.biblioteca.enums.*;
import org.fcuevas.biblioteca.util.OracleDB;
import org.fcuevas.biblioteca.dao.interfaces.IPrestamoDAO;
import org.fcuevas.biblioteca.dto.PrestamoDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class PrestamoDAO implements IPrestamoDAO {
    @Override
    public Integer crear(PrestamoDTO p) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            // Si fechaPrestamo viene null, usamos la fecha actual desde la app
            String sql = "INSERT INTO PRESTAMOS (CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, ESTADO, MULTA_APLICADA, DIAS_ATRASO) " +
                    "VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getCodigoMaterial());
                ps.setString(2, p.getRutUsuario());
                Date f = (p.getFechaPrestamo() == null) ? new Date(System.currentTimeMillis())
                        : Date.valueOf(p.getFechaPrestamo());
                ps.setDate(3, f);
                ps.setString(4, (p.getEstado() == null ? EstadoPrestamo.ACTIVO : p.getEstado()).name());
                ps.setDouble(5, p.getMultaAplicada());
                ps.setInt(6, p.getDiasAtraso());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                    throw new SQLException("No se obtuvo CODIGO generado para PRESTAMOS");
                }
            }
        }
    }

    @Override
    public Optional<PrestamoDTO> buscarPorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(transformar(rs));
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<PrestamoDTO> listarTodos() throws SQLException {
        List<PrestamoDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS ORDER BY FECHA_PRESTAMO DESC, CODIGO DESC";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(transformar(rs));
            }
        }
        return lista;
    }

    @Override
    public List<PrestamoDTO> listarActivos() throws SQLException {
        List<PrestamoDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS WHERE ESTADO = 'ACTIVO' ORDER BY FECHA_PRESTAMO DESC";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(transformar(rs));
            }
        }
        return lista;
    }

    @Override
    public List<PrestamoDTO> listarPorUsuario(String rutUsuario) throws SQLException {
        List<PrestamoDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS WHERE RUT_USUARIO = ? ORDER BY FECHA_PRESTAMO DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, rutUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(transformar(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<PrestamoDTO> listarActivosPorUsuario(String rutUsuario) throws SQLException {
        List<PrestamoDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS WHERE RUT_USUARIO = ? AND ESTADO = 'ACTIVO' ORDER BY FECHA_PRESTAMO DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, rutUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(transformar(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<PrestamoDTO> listarPorMaterial(Integer codigoMaterial) throws SQLException {
        List<PrestamoDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, RUT_USUARIO, FECHA_PRESTAMO, FECHA_DEVOLUCION, " +
                    "ESTADO, MULTA_APLICADA, DIAS_ATRASO " +
                    "FROM PRESTAMOS WHERE CODIGO_MATERIAL = ? ORDER BY FECHA_PRESTAMO DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigoMaterial);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(transformar(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public boolean registrarDevolucion(Integer codigoPrestamo, LocalDate fechaDevolucion, int diasAtraso, double multaAplicada) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE PRESTAMOS SET FECHA_DEVOLUCION = ?, DIAS_ATRASO = ?, MULTA_APLICADA = ?, ESTADO = 'DEVUELTO' " +
                    "WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, (fechaDevolucion == null) ? null : Date.valueOf(fechaDevolucion));
                ps.setInt(2, diasAtraso);
                ps.setDouble(3, multaAplicada);
                ps.setInt(4, codigoPrestamo);
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean cambiarEstado(Integer codigoPrestamo, EstadoPrestamo estado) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE PRESTAMOS SET ESTADO = ? WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, estado.name());
                ps.setInt(2, codigoPrestamo);
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean actualizarMontos(Integer codigoPrestamo, int diasAtraso, double multaAplicada) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE PRESTAMOS SET DIAS_ATRASO = ?, MULTA_APLICADA = ? WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, diasAtraso);
                ps.setDouble(2, multaAplicada);
                ps.setInt(3, codigoPrestamo);
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean eliminarPorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "DELETE FROM PRESTAMOS WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigo);
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean existePorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT 1 FROM PRESTAMOS WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    @Override
    public boolean existePrestamoActivoPorMaterial(Integer codigoMaterial) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT 1 FROM PRESTAMOS WHERE CODIGO_MATERIAL = ? AND ESTADO = 'ACTIVO'";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigoMaterial);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    @Override
    public long contarPrestamosActivosPorUsuario(String rutUsuario) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT COUNT(1) FROM PRESTAMOS WHERE RUT_USUARIO = ? AND ESTADO = 'ACTIVO'";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, rutUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                    return 0L;
                }
            }
        }
    }

    private static PrestamoDTO transformar(ResultSet rs) throws SQLException {
        PrestamoDTO p = new PrestamoDTO();
        p.setCodigo(rs.getInt("CODIGO"));
        p.setCodigoMaterial(rs.getInt("CODIGO_MATERIAL"));
        p.setRutUsuario(rs.getString("RUT_USUARIO"));

        Date fPrest = rs.getDate("FECHA_PRESTAMO");
        p.setFechaPrestamo(fPrest == null ? null : fPrest.toLocalDate());

        Date fDev = rs.getDate("FECHA_DEVOLUCION");
        p.setFechaDevolucion(fDev == null ? null : fDev.toLocalDate());

        String est = rs.getString("ESTADO");
        p.setEstado(est == null ? null : EstadoPrestamo.desdeBD(est));

        p.setMultaAplicada(rs.getDouble("MULTA_APLICADA"));
        p.setDiasAtraso(rs.getInt("DIAS_ATRASO"));
        return p;
    }
}