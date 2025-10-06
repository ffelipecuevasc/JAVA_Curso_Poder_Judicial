package org.fcuevas.curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        ConexionOracle c = new ConexionOracle();
        probarConexion(c);
        probarCRUD(c);
    }

    private static void probarCRUD(ConexionOracle c) {
        // 1) Crear objeto Caso
        c.conectar();
        Caso caso = new Caso("RIT-101-2025", "Pérez c/ Estado", LocalDate.now(), EstadoCaso.ABIERTO);
        // 2) INSERT
        Long id = c.insertarCaso(caso);
        System.out.println("INSERT → ID generado: " + id);
        // 3) SELECT por ROL
        Caso encontrado = c.buscarCasoPorRol("RIT-101-2025");
        System.out.println("SELECT → " + encontrado);
        // 4) UPDATE estado por ROL
        int filasUpd = c.actualizarEstadoPorRol("RIT-101-2025", EstadoCaso.EN_TRAMITE);
        System.out.println("UPDATE → filas afectadas: " + filasUpd);
        // 5) LISTAR todos
        List<Caso> todos = c.listarCasos();
        System.out.println("LISTAR → total: " + todos.size());
        todos.forEach(System.out::println);
        // 6) DELETE por ROL
        int filasDel = c.eliminarCasoPorRol("RIT-101-2025");
        System.out.println("DELETE → filas afectadas: " + filasDel);
        c.desconectar();
    }

    private static void probarConexion(ConexionOracle c) {
        c.conectar();
        c.probarConexion();
        c.desconectar();
    }
}