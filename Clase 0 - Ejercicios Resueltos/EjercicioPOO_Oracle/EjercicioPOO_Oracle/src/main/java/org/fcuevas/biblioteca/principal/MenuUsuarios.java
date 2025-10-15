package org.fcuevas.biblioteca.principal;

import org.fcuevas.biblioteca.dto.UsuarioDTO;
import org.fcuevas.biblioteca.service.interfaces.IUsuarioService;

import java.util.Scanner;

public class MenuUsuarios {
    private IUsuarioService usuarioService;
    private Scanner sc = new Scanner(System.in);

    public MenuUsuarios(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** Bucle principal del submenú CRUD de usuarios. */
    public void ejecutar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> listarUsuarios();
                case 3 -> buscarUsuarioPorRut();
                case 4 -> actualizarUsuario();
                case 5 -> eliminarUsuario();
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 6);
    }

    private void mostrarOpciones() {
        System.out.println("\n=== Gestión de Usuarios ===");
        System.out.println("1) Crear usuario");
        System.out.println("2) Listar usuarios");
        System.out.println("3) Buscar por RUT");
        System.out.println("4) Actualizar usuario");
        System.out.println("5) Eliminar usuario");
        System.out.println("6) Volver");
    }

    private void crearUsuario() {
        try {
            System.out.println("\n-- Crear usuario --");
            String rut = leerLinea("RUT: ");
            String nombre = leerLinea("Nombre: ");
            String email = leerLinea("Email: ");
            int cupoMax = leerEntero("Cupo máximo: ");

            UsuarioDTO u = new UsuarioDTO(rut, nombre, email, cupoMax);
            boolean ok = usuarioService.crearUsuario(u);
            System.out.println(ok ? "Usuario creado." : "No se pudo crear el usuario.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarUsuarios() {
        try {
            System.out.println("\n-- Listar usuarios --");
            var lista = usuarioService.listarUsuarios();
            if (lista.isEmpty()) {
                System.out.println("(Sin usuarios)");
                return;
            }
            lista.forEach(u -> System.out.printf(
                    "- %s | %s | %s | cupoMax=%d | cupoActual=%d%n",
                    u.getRut(), u.getNombre(), u.getEmail(), u.getCupoMax(), u.getCupoActual()));
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void buscarUsuarioPorRut() {
        try {
            System.out.println("\n-- Buscar usuario por RUT --");
            String rut = leerLinea("RUT: ");
            var opt = usuarioService.buscarPorRut(rut);
            if (opt.isPresent()) {
                var u = opt.get();
                System.out.printf("Encontrado: %s | %s | %s | cupoMax=%d | cupoActual=%d%n",
                        u.getRut(), u.getNombre(), u.getEmail(), u.getCupoMax(), u.getCupoActual());
            } else {
                System.out.println("No existe un usuario con ese RUT.");
            }
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void actualizarUsuario() {
        try {
            System.out.println("\n-- Actualizar usuario --");
            String rut = leerLinea("RUT (a actualizar): ");
            var existente = usuarioService.buscarPorRut(rut);
            if (existente.isEmpty()) {
                System.out.println("No existe un usuario con ese RUT.");
                return;
            }
            String nombre = leerLinea("Nuevo nombre: ");
            String email = leerLinea("Nuevo email: ");
            int cupoMax = leerEntero("Nuevo cupo máximo: ");
            int cupoActual = leerEntero("Nuevo cupo actual: ");

            UsuarioDTO u = new UsuarioDTO();
            u.setRut(rut);
            u.setNombre(nombre);
            u.setEmail(email);
            u.setCupoMax(cupoMax);
            u.setCupoActual(cupoActual);

            boolean ok = usuarioService.actualizarUsuario(u);
            System.out.println(ok ? "Usuario actualizado." : "No se pudo actualizar el usuario.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void eliminarUsuario() {
        try {
            System.out.println("\n-- Eliminar usuario --");
            String rut = leerLinea("RUT: ");
            String conf = leerLinea("Confirmar eliminación (S/N): ");
            if (!conf.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }
            boolean ok = usuarioService.eliminarUsuario(rut);
            System.out.println(ok ? "Usuario eliminado." : "No se eliminó (puede no existir).");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // -------- utilidades locales de lectura/errores --------

    private String leerLinea(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
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

    private void mostrarError(Exception e) {
        String msg = e.getMessage();
        System.out.println("Error: " + (msg == null ? e.getClass().getSimpleName() : msg));
    }
}
