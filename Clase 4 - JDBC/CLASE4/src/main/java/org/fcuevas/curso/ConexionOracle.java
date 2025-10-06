package org.fcuevas.curso;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConexionOracle {

    private Connection con;
    private String url;
    private String usuario;
    private String clave;

    public ConexionOracle() {
        this.url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        this.usuario = "CASOS_APP";
        this.clave = "casos";
    }

    //Crea la conexión a Oracle DataBase
    public void conectar() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            this.con = DriverManager.getConnection(url, usuario, clave);
            System.out.println("[JDBC] Conexión exitosa a Oracle DB.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[JDBC] No se pudo conectar a Oracle DB: " + e.getMessage());

        }
    }

    public void desconectar() {
        if (this.con != null) {
            try {
                this.con.close();
                System.out.println("[JDBC] Desconexión exitosa de Oracle DB.");
            } catch (SQLException e) {
                System.out.println("[JDBC] No se pudo desconectar de Oracle DB: " + e.getMessage());
            } finally {
                this.con = null;
            }
        }
    }

    public void probarConexion() {
        System.out.println("[JDBC] Realizando una prueba de conexión a la tabla USUARIO de Oracle DB.");
        if (this.con != null) {
            String sqlInfo = "SELECT SYSDATE AS FECHA_BD, USER AS USUARIO FROM DUAL";
            try {
                Statement st = this.con.createStatement();
                ResultSet rs = st.executeQuery(sqlInfo);
                if(rs.next()) {
                    java.sql.Timestamp fechaBd = rs.getTimestamp("FECHA_BD");
                    String usuario = rs.getString("USUARIO");
                    System.out.println("[JDBC] Prueba exitosa.\n\t- Fecha: " + fechaBd + "\n\t- Usuario:" + usuario);
                }
                st.close();
                rs.close();
            } catch (SQLException e) {
                System.out.println("[JDBC] Error en la prueba de conexión: " + e.getMessage());
            }
        }
    }

    // ------------------------ CRUD con Statement ------------------------

    /**
     * INSERT en CASO usando Statement.
     * Usa literal ANSI DATE 'yyyy-mm-dd' para la fecha.
     * Devuelve el ID generado o null si no se pudo obtener.
     */
    public Long insertarCaso(Caso caso) {
        if (con == null) { System.out.println("Sin conexión."); return null; }

        String rol = caso.getRol();
        String car = caso.getCaratulado();
        String fecha = "DATE '" + caso.getFechaIngreso() + "'";
        String est = caso.getEstado().name();

        String sqlInsert = "INSERT INTO CASO (ROL, CARATULADO, FECHA_INGRESO, ESTADO) " +
                "VALUES ('" + rol + "', '" + car + "', " + fecha + ", '" + est + "')";
        try (Statement s = con.createStatement()) {
            int filas = s.executeUpdate(sqlInsert);
            System.out.println("[JDBC] Filas insertadas: " + filas);
        } catch (SQLException e) {
            System.out.println("[JDBC] Error INSERT: " + e.getMessage());
            return null;
        }

        // Recuperar el ID por el ROL (requiere índice único en ROL)
        String sqlId = "SELECT ID FROM CASO WHERE ROL = '" + rol + "'";
        try (Statement s2 = con.createStatement();
             ResultSet r2 = s2.executeQuery(sqlId)) {
            if (r2.next()) {
                long id = r2.getLong(1);
                caso.setId(id);
                return id;
            }
        } catch (SQLException e) {
            System.out.println("[JDBC] Error al obtener ID: " + e.getMessage());
        }
        return null;
    }

    /** SELECT * FROM CASO WHERE ROL = '...'  */
    public Caso buscarCasoPorRol(String rolBuscado) {
        if (con == null) { System.out.println("Sin conexión."); return null; }
        String rol = rolBuscado;
        String sql = "SELECT ID, ROL, CARATULADO, FECHA_INGRESO, ESTADO FROM CASO WHERE ROL = '" + rol + "'";
        try (Statement s = con.createStatement(); ResultSet r = s.executeQuery(sql)) {
            if (r.next()) {
                return mapRowToCaso(r);
            }
        } catch (SQLException e) {
            System.out.println("[JDBC] Error SELECT por ROL: " + e.getMessage());
        }
        return null;
    }

    /** UPDATE CASO SET ESTADO = '...' WHERE ROL = '...'  */
    public int actualizarEstadoPorRol(String rolBuscado, EstadoCaso nuevoEstado) {
        if (con == null) { System.out.println("Sin conexión."); return 0; }
        String rol = rolBuscado;
        String est = nuevoEstado.name();
        String sql = "UPDATE CASO SET ESTADO = '" + est + "' WHERE ROL = '" + rol + "'";
        try (Statement s = con.createStatement()) {
            int filas = s.executeUpdate(sql);
            System.out.println("[JDBC] UPDATE filas: " + filas);
            return filas;
        } catch (SQLException e) {
            System.out.println("[JDBC] Error UPDATE: " + e.getMessage());
            return 0;
        }
    }

    /** DELETE FROM CASO WHERE ROL = '...' */
    public int eliminarCasoPorRol(String rolBuscado) {
        if (con == null) { System.out.println("Sin conexión."); return 0; }
        String rol = rolBuscado;
        String sql = "DELETE FROM CASO WHERE ROL = '" + rol + "'";
        try (Statement s = con.createStatement()) {
            int filas = s.executeUpdate(sql);
            System.out.println("[JDBC] DELETE filas: " + filas);
            return filas;
        } catch (SQLException e) {
            System.out.println("[JDBC] Error DELETE: " + e.getMessage());
            return 0;
        }
    }

    /** SELECT * FROM CASO ORDER BY FECHA_INGRESO DESC, ROL ASC */
    public List<Caso> listarCasos() {
        if (con == null) { System.out.println("Sin conexión."); return List.of(); }
        String sql = "SELECT ID, ROL, CARATULADO, FECHA_INGRESO, ESTADO " +
                "FROM CASO ORDER BY FECHA_INGRESO DESC, ROL ASC";
        List<Caso> lista = new ArrayList<>();
        try (Statement s = con.createStatement(); ResultSet r = s.executeQuery(sql)) {
            while (r.next()) {
                lista.add(mapRowToCaso(r));
            }
        } catch (SQLException e) {
            System.out.println("[JDBC] Error LISTAR: " + e.getMessage());
        }
        return lista;
    }

    // ------------------------ Métodos auxiliares internos ------------------------

    private static Caso mapRowToCaso(ResultSet r) throws SQLException {
        Long id = r.getLong("ID");
        String rol = r.getString("ROL");
        String car = r.getString("CARATULADO");
        LocalDate fecha = r.getDate("FECHA_INGRESO").toLocalDate();
        String est = r.getString("ESTADO");
        EstadoCaso estado = EstadoCaso.valueOf(est);
        return new Caso(id, rol, car, fecha, estado);
    }
}