package org.fcuevas.biblioteca.principal;

import org.fcuevas.biblioteca.service.interfaces.IUsuarioService;
import org.fcuevas.biblioteca.service.implementaciones.UsuarioService;
import org.fcuevas.biblioteca.util.OracleDB;

import java.util.Scanner;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private IUsuarioService usuarioService = new UsuarioService();
    private MenuUsuarios menuUsuarios = new MenuUsuarios(usuarioService);

    public static void main(String[] args) {
        new Principal().ejecutar();
    }

    private void ejecutar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> menuUsuarios.ejecutar();
                case 2 -> System.out.println("Gestión de Materiales (pendiente).");
                case 3 -> System.out.println("Gestión de Préstamos (pendiente).");
                case 4 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 4);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=== Biblioteca — Menú Principal ===");
        System.out.println("1) Gestión de Usuarios (CRUD)");
        System.out.println("2) Gestión de Materiales (CRUD)");
        System.out.println("3) Gestión de Préstamos (CRUD + operaciones)");
        System.out.println("4) Salir");
    }

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
}
