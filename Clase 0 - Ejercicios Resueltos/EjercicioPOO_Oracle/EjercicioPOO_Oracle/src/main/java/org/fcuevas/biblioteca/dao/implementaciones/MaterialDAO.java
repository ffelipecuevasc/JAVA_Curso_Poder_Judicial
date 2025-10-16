package org.fcuevas.biblioteca.dao.implementaciones;

import org.fcuevas.biblioteca.dao.interfaces.IMaterialDAO;
import org.fcuevas.biblioteca.dto.AudioLibroDTO;
import org.fcuevas.biblioteca.dto.LibroDTO;
import org.fcuevas.biblioteca.dto.MaterialDTO;
import org.fcuevas.biblioteca.dto.RevistaDTO;
import org.fcuevas.biblioteca.enums.TipoMaterial;
import org.fcuevas.biblioteca.util.OracleDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialDAO implements IMaterialDAO {
    @Override
    public Integer crearLibro(MaterialDTO base, LibroDTO libro) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            con.setAutoCommit(false);
            try {
                // 1) Insertar MATERIAL (IDENTITY)
                Integer codigoMaterial;
                String sqlMaterial = "INSERT INTO MATERIAL (TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlMaterial, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, base.getTitulo());
                    ps.setString(2, base.getTipo().name());
                    ps.setInt(3, base.getDiasBase());
                    ps.setString(4, aSN(base.isDisponible()));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("No se obtuvo CODIGO generado para MATERIAL");
                        codigoMaterial = rs.getInt(1);
                    }
                }

                // 2) Insertar MATERIAL_LIBRO
                String sqlLibro = "INSERT INTO MATERIAL_LIBRO (CODIGO_MATERIAL, AUTOR, ANIO, ISBN) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlLibro)) {
                    ps.setInt(1, codigoMaterial);
                    ps.setString(2, libro.getAutor());
                    ps.setInt(3, libro.getAño());
                    ps.setString(4, libro.getIsbn());
                    ps.executeUpdate();
                }

                con.commit();
                return codigoMaterial;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public Integer crearRevista(MaterialDTO base, RevistaDTO revista) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            con.setAutoCommit(false);
            try {
                // 1) MATERIAL
                Integer codigoMaterial;
                String sqlMaterial = "INSERT INTO MATERIAL (TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlMaterial, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, base.getTitulo());
                    ps.setString(2, base.getTipo().name());
                    ps.setInt(3, base.getDiasBase());
                    ps.setString(4, aSN(base.isDisponible()));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("No se obtuvo CODIGO generado para MATERIAL");
                        codigoMaterial = rs.getInt(1);
                    }
                }
                // 2) REVISTA
                String sqlRevista = "INSERT INTO MATERIAL_REVISTA (CODIGO_MATERIAL, NUMERO_EDICION, PERIODICIDAD) VALUES (?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlRevista)) {
                    ps.setInt(1, codigoMaterial);
                    ps.setInt(2, revista.getNumeroEdicion());
                    ps.setString(3, revista.getPeriodicidad());
                    ps.executeUpdate();
                }

                con.commit();
                return codigoMaterial;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public Integer crearAudiolibro(MaterialDTO base, AudioLibroDTO audioLibro) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            con.setAutoCommit(false);
            try {
                // 1) MATERIAL
                Integer codigoMaterial;
                String sqlMaterial = "INSERT INTO MATERIAL (TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlMaterial, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, base.getTitulo());
                    ps.setString(2, base.getTipo().name());
                    ps.setInt(3, base.getDiasBase());
                    ps.setString(4, aSN(base.isDisponible()));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("No se obtuvo CODIGO generado para MATERIAL");
                        codigoMaterial = rs.getInt(1);
                    }
                }
                // 2) AUDIOLIBRO
                String sqlAudio = "INSERT INTO MATERIAL_AUDIOLIBRO (CODIGO_MATERIAL, NARRADOR, DURACION_MIN, LICENCIA_DIGITAL) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sqlAudio)) {
                    ps.setInt(1, codigoMaterial);
                    ps.setString(2, audioLibro.getNarrador());
                    ps.setInt(3, audioLibro.getDuracionMin());
                    ps.setString(4, aSN(audioLibro.getLicenciaDigital()));
                    ps.executeUpdate();
                }

                con.commit();
                return codigoMaterial;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public Optional<MaterialDTO> buscarPorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE FROM MATERIAL WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return Optional.of(transformarMaterial(rs));
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public Optional<LibroDTO> buscarDetalleLibro(Integer codigoMaterial) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, AUTOR, ANIO, ISBN FROM MATERIAL_LIBRO WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigoMaterial);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return Optional.empty();
                    LibroDTO d = new LibroDTO();
                    d.setCodigo(rs.getInt("CODIGO"));
                    d.setCodigoMaterial(rs.getInt("CODIGO_MATERIAL"));
                    d.setAutor(rs.getString("AUTOR"));
                    d.setAño(rs.getInt("ANIO"));
                    d.setIsbn(rs.getString("ISBN"));
                    return Optional.of(d);
                }
            }
        }
    }

    @Override
    public Optional<RevistaDTO> buscarDetalleRevista(Integer codigoMaterial) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, NUMERO_EDICION, PERIODICIDAD FROM MATERIAL_REVISTA WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigoMaterial);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return Optional.empty();
                    RevistaDTO d = new RevistaDTO();
                    d.setCodigo(rs.getInt("CODIGO"));
                    d.setCodigoMaterial(rs.getInt("CODIGO_MATERIAL"));
                    d.setNumeroEdicion(rs.getInt("NUMERO_EDICION"));
                    d.setPeriodicidad(rs.getString("PERIODICIDAD"));
                    return Optional.of(d);
                }
            }
        }
    }

    @Override
    public Optional<AudioLibroDTO> buscarDetalleAudioLibro(Integer codigoMaterial) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, CODIGO_MATERIAL, NARRADOR, DURACION_MIN, LICENCIA_DIGITAL FROM MATERIAL_AUDIOLIBRO WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigoMaterial);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return Optional.empty();
                    AudioLibroDTO d = new AudioLibroDTO();
                    d.setCodigo(rs.getInt("CODIGO"));
                    d.setCodigoMaterial(rs.getInt("CODIGO_MATERIAL"));
                    d.setNarrador(rs.getString("NARRADOR"));
                    d.setDuracionMin(rs.getInt("DURACION_MIN"));
                    d.setLicenciaDigital(aBool(rs.getString("LICENCIA_DIGITAL")));
                    return Optional.of(d);
                }
            }
        }
    }

    @Override
    public List<MaterialDTO> listarTodos() throws SQLException {
        List<MaterialDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE FROM MATERIAL ORDER BY TITULO";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(transformarMaterial(rs));
            }
        }
        return lista;
    }

    @Override
    public List<MaterialDTO> listarPorTipo(TipoMaterial tipo) throws SQLException {
        List<MaterialDTO> lista = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE FROM MATERIAL WHERE TIPO_MATERIAL = ? ORDER BY TITULO";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tipo.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) lista.add(transformarMaterial(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<MaterialDTO> listarDisponiblesPorTipo(TipoMaterial tipo) throws SQLException {
        List<MaterialDTO> out = new ArrayList<>();
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT CODIGO, TITULO, TIPO_MATERIAL, DIAS_BASE, DISPONIBLE " +
                    "FROM MATERIAL WHERE TIPO_MATERIAL = ? AND DISPONIBLE = 'S' ORDER BY TITULO";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tipo.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) out.add(transformarMaterial(rs));
                }
            }
        }
        return out;
    }

    @Override
    public boolean actualizarMaterial(MaterialDTO material) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE MATERIAL SET TITULO = ?, TIPO_MATERIAL = ?, DIAS_BASE = ?, DISPONIBLE = ? WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, material.getTitulo());
                ps.setString(2, material.getTipo().name());
                ps.setInt(3, material.getDiasBase());
                ps.setString(4, aSN(material.isDisponible()));
                ps.setInt(5, material.getCodigo());
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean actualizarDetalleLibro(LibroDTO libro) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE MATERIAL_LIBRO SET AUTOR = ?, ANIO = ?, ISBN = ? WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, libro.getAutor());
                ps.setInt(2, libro.getAño());
                ps.setString(3, libro.getIsbn());
                ps.setInt(4, libro.getCodigoMaterial());
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean actualizarDetalleRevista(RevistaDTO revista) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE MATERIAL_REVISTA SET NUMERO_EDICION = ?, PERIODICIDAD = ? WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, revista.getNumeroEdicion());
                ps.setString(2, revista.getPeriodicidad());
                ps.setInt(3, revista.getCodigoMaterial());
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean actualizarDetalleAudioLibro(AudioLibroDTO audioLibro) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE MATERIAL_AUDIOLIBRO SET NARRADOR = ?, DURACION_MIN = ?, LICENCIA_DIGITAL = ? WHERE CODIGO_MATERIAL = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, audioLibro.getNarrador());
                ps.setInt(2, audioLibro.getDuracionMin());
                ps.setString(3, aSN(audioLibro.getLicenciaDigital()));
                ps.setInt(4, audioLibro.getCodigoMaterial());
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean cambiarDisponibilidad(Integer codigoMaterial, boolean disponible) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "UPDATE MATERIAL SET DISPONIBLE = ? WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, aSN(disponible));
                ps.setInt(2, codigoMaterial);
                return ps.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean eliminarPorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            con.setAutoCommit(false);
            try {
                // 1) Determinar tipo para borrar detalle correcto
                String sqlTipo = "SELECT TIPO_MATERIAL FROM MATERIAL WHERE CODIGO = ?";
                TipoMaterial tipo;
                try (PreparedStatement ps = con.prepareStatement(sqlTipo)) {
                    ps.setInt(1, codigo);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { con.rollback(); return false; }
                        tipo = TipoMaterial.desdeBD(rs.getString(1));
                    }
                }

                // 2) Borrar detalle según tipo
                if (tipo == TipoMaterial.LIBRO) {
                    String sql = "DELETE FROM MATERIAL_LIBRO WHERE CODIGO_MATERIAL = ?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, codigo);
                        ps.executeUpdate();
                    }
                } else if (tipo == TipoMaterial.REVISTA) {
                    String sql = "DELETE FROM MATERIAL_REVISTA WHERE CODIGO_MATERIAL = ?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, codigo);
                        ps.executeUpdate();
                    }
                } else if (tipo == TipoMaterial.AUDIOLIBRO) {
                    String sql = "DELETE FROM MATERIAL_AUDIOLIBRO WHERE CODIGO_MATERIAL = ?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, codigo);
                        ps.executeUpdate();
                    }
                }

                // 3) Borrar MATERIAL (fallará si hay FKs de PRESTAMOS)
                String sqlDel = "DELETE FROM MATERIAL WHERE CODIGO = ?";
                int filas;
                try (PreparedStatement ps = con.prepareStatement(sqlDel)) {
                    ps.setInt(1, codigo);
                    filas = ps.executeUpdate();
                }

                con.commit();
                return filas == 1;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean existePorCodigo(Integer codigo) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT 1 FROM MATERIAL WHERE CODIGO = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    @Override
    public boolean existeLibroPorIsbn(String isbn) throws SQLException {
        try (Connection con = OracleDB.establecerConexion()) {
            String sql = "SELECT 1 FROM MATERIAL_LIBRO WHERE ISBN = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, isbn);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    private static String aSN(boolean b) {
        return b ? "S" : "N";
    }

    private static boolean aBool(String sn) {
        return sn != null && sn.trim().equalsIgnoreCase("S");
    }

    private static MaterialDTO transformarMaterial(ResultSet rs) throws SQLException {
        MaterialDTO m = new MaterialDTO();
        m.setCodigo(rs.getInt("CODIGO"));
        m.setTitulo(rs.getString("TITULO"));
        m.setTipo(TipoMaterial.desdeBD(rs.getString("TIPO_MATERIAL")));
        m.setDiasBase(rs.getInt("DIAS_BASE"));
        m.setDisponible(aBool(rs.getString("DISPONIBLE")));
        return m;
    }
}
