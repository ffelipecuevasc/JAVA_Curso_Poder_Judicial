package org.fcuevas.biblioteca.util;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/*
* OracleDB es una clase utilitaria estática para centralizar la configuración (leer db.properties)
* y obtener conexiones JDBC a Oracle. No usa pool; cada llamada a getConnection() abre una conexión
* nueva (que debes cerrar con try-with-resources).
* */
public final class OracleDB {

    /*
    * Constantes y Properties
    * - ARCHIVO = "db.properties".
    * - PROPIEDADES = contenedor donde se cargan las claves/valores.
    * */
    private static final String ARCHIVO = "db.properties";
    private static final Properties PROPIEDADES = new Properties();

    /*
    * Bloque static { ... }
    * - Se ejecuta una sola vez cuando la clase se carga.
    * - Busca db.properties en el classpath (src/main/resources), lo carga y, si existe db.driver,
    *   hace Class.forName(...). (Con ojdbc11 no es obligatorio cargar el driver; se auto-registra)
    * */
    static {
        try (var in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ARCHIVO)) {
            if (in == null) throw new IllegalStateException("[ERROR] No se encontró " + ARCHIVO + " en el classpath");
            PROPIEDADES.load(in);
            // Cargar driver si se especifica aunque el driver ojdbc11 se autoregistra
            var drv = PROPIEDADES.getProperty("db.driver");
            if (drv != null && !drv.isBlank()) Class.forName(drv);
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /*
    * Constructor private OracleDB()
    * - Evita que alguien haga new OracleDB(). Es la señal de “clase de utilidades”.
    * */
    private OracleDB() {

    }

    /*
    * Metodo establecerConexion()
    * - Lee db.url, db.user y db.password desde PROPIEDADES (usando extraerPropiedad para validar que existan).
    * - Retorna un Connection via DriverManager.getConnection(...).
    * - Importante: cada invocación devuelve una conexión nueva; recuerda cerrarla.
    * */
    public static Connection establecerConexion() throws SQLException {
        String url  = extraerPropiedad("db.url");
        String user = extraerPropiedad("db.user");
        String pwd  = extraerPropiedad("db.password");
        Connection con = DriverManager.getConnection(url, user, pwd);
        return con;
    }

    /*
    * extraerPropiedad(String propiedad)
    * - Obtiene el valor de una clave y falla explícitamente si falta o viene en blanco.
    * - Te ahorra errores silenciosos.
    * */
    private static String extraerPropiedad(String propiedad) {
        var v = PROPIEDADES.getProperty(propiedad);
        if (v == null || v.isBlank()) throw new IllegalStateException("[ERROR] Falta propiedad: " + propiedad);
        return v;
    }
}
