package org.fcuevas.biblioteca.principal;

import org.fcuevas.biblioteca.dao.implementaciones.MaterialDAO;
import org.fcuevas.biblioteca.service.implementaciones.MaterialService;
import org.fcuevas.biblioteca.service.implementaciones.PrestamoService;
import org.fcuevas.biblioteca.service.implementaciones.UsuarioService;
import org.fcuevas.biblioteca.service.interfaces.IMaterialService;
import org.fcuevas.biblioteca.service.interfaces.IPrestamoService;
import org.fcuevas.biblioteca.service.interfaces.IUsuarioService;
import org.fcuevas.biblioteca.util.OracleDB;
import java.util.Scanner;

public class Principal {

    private final Scanner sc = new Scanner(System.in);

    // Servicios
    private final IUsuarioService usuarioService = new UsuarioService();
    private final IPrestamoService prestamoService = new PrestamoService();
    private final IMaterialService materialService =
            new MaterialService(new MaterialDAO(), prestamoService); // inyecta PrestamoService

    // Menús
    private final MenuUsuarios menuUsuarios = new MenuUsuarios(usuarioService);
    private final MenuMateriales menuMateriales = new MenuMateriales(materialService);
    private final MenuPrestamos menuPrestamos = new MenuPrestamos(prestamoService);

    public static void main(String[] args) {
        new Principal().ejecutar();
    }

    private void ejecutar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                // Gestión de Usuarios (CRUD)
                case 1 -> menuUsuarios.ejecutar();
                // Gestión de Materiales (CRUD)
                case 2 -> menuMateriales.ejecutar();
                // Gestión de Préstamos (CRUD + ops)
                case 3 -> menuPrestamos.ejecutar();
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
