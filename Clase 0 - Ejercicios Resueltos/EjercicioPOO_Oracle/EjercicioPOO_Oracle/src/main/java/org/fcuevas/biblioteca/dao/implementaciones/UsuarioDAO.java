package org.fcuevas.biblioteca.dao.implementaciones;

import org.fcuevas.biblioteca.dao.interfaces.IUsuarioDAO;
import org.fcuevas.biblioteca.dto.UsuarioDTO;
import org.fcuevas.biblioteca.util.OracleDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IUsuarioDAO {
    @Override
    public boolean crear(UsuarioDTO usuario) throws SQLException {
        String SQL_INSERT = "INSERT INTO USUARIO (RUT, NOMBRE, EMAIL, CUPO_MAX, CUPO_ACTUAL) VALUES (?,?,?,?,?)";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, usuario.getRut());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getEmail());
            ps.setInt(4, usuario.getCupoMax());
            ps.setInt(5, usuario.getCupoActual());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<UsuarioDTO> buscarPorRut(String rut) throws SQLException {
        String SQL_SELECT_BY_RUT = "SELECT RUT, NOMBRE, EMAIL, CUPO_MAX, CUPO_ACTUAL, FECHA_CREACION FROM USUARIO WHERE RUT = ?";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_RUT)) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<UsuarioDTO> listarTodos() throws SQLException {
        String SQL_SELECT_ALL = "SELECT RUT, NOMBRE, EMAIL, CUPO_MAX, CUPO_ACTUAL, FECHA_CREACION FROM USUARIO ORDER BY NOMBRE";
        List<UsuarioDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public boolean actualizar(UsuarioDTO usuario) throws SQLException {
        String SQL_UPDATE = "UPDATE USUARIO SET NOMBRE = ?, EMAIL = ?, CUPO_MAX = ?, CUPO_ACTUAL = ? WHERE RUT = ?";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getCupoMax());
            ps.setInt(4, usuario.getCupoActual());
            ps.setString(5, usuario.getRut());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean eliminarPorRut(String rut) throws SQLException {
        String SQL_DELETE = "DELETE FROM USUARIO WHERE RUT = ?";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setString(1, rut);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean existePorRut(String rut) throws SQLException {
        String SQL_EXISTE_RUT = "SELECT 1 FROM USUARIO WHERE RUT = ?";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_RUT)) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existePorEmail(String email) throws SQLException {
        String SQL_EXISTE_EMAIL = "SELECT 1 FROM USUARIO WHERE EMAIL = ?";
        try (Connection con = OracleDB.establecerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ---- Helpers ----
    private UsuarioDTO mapear(ResultSet rs) throws SQLException {
        UsuarioDTO u = new UsuarioDTO();
        u.setRut(rs.getString("RUT"));
        u.setNombre(rs.getString("NOMBRE"));
        u.setEmail(rs.getString("EMAIL"));
        u.setCupoMax(rs.getInt("CUPO_MAX"));
        u.setCupoActual(rs.getInt("CUPO_ACTUAL"));
        u.setFechaCreacion(rs.getDate("FECHA_CREACION").toLocalDate());
        return u;
    }
}
