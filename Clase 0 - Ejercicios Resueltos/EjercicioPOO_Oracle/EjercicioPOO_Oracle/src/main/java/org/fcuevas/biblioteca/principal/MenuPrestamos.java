package org.fcuevas.biblioteca.principal;

import org.fcuevas.biblioteca.dto.PrestamoDTO;
import org.fcuevas.biblioteca.enums.EstadoPrestamo;
import org.fcuevas.biblioteca.service.interfaces.IPrestamoService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrestamos {
    private final IPrestamoService prestamoService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuPrestamos(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    /** Bucle principal del submenú de Préstamos. */
    public void ejecutar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1  -> crearPrestamo();
                case 2  -> listarTodos();
                case 3  -> listarActivos();
                case 4  -> listarPorUsuario();
                case 5  -> listarPorMaterial();
                case 6  -> registrarDevolucion();
                case 7  -> cambiarEstado();
                case 8  -> actualizarMontos();
                case 9  -> eliminarPrestamo();
                case 10 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 10);
    }

    private void mostrarOpciones() {
        System.out.println("\n=== Gestión de Préstamos ===");
        System.out.println("1)  Crear préstamo");
        System.out.println("2)  Listar TODOS");
        System.out.println("3)  Listar ACTIVOS");
        System.out.println("4)  Listar por USUARIO (RUT)");
        System.out.println("5)  Listar por MATERIAL (código)");
        System.out.println("6)  Registrar DEVOLUCIÓN");
        System.out.println("7)  Cambiar ESTADO");
        System.out.println("8)  Actualizar MONTOS (multa/atraso)");
        System.out.println("9)  Eliminar préstamo");
        System.out.println("10) Volver");
    }

    // ───────────────────────────────
    // CREACIÓN
    // ───────────────────────────────

    private void crearPrestamo() {
        try {
            System.out.println("\n-- Crear préstamo --");
            int codigoMaterial = leerEnteroPositivo("Código del material: ");
            String rutUsuario = leerLinea("RUT del usuario: ");
            LocalDate fechaPrestamo = leerFechaONull("Fecha de préstamo (YYYY-MM-DD, ENTER para hoy): ");
            if (fechaPrestamo == null) fechaPrestamo = LocalDate.now();

            PrestamoDTO p = new PrestamoDTO(codigoMaterial, rutUsuario, fechaPrestamo);
            Integer generado = prestamoService.crearPrestamo(p);
            System.out.println("Préstamo creado. Código: " + generado);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // LISTADOS / BÚSQUEDAS
    // ───────────────────────────────

    private void listarTodos() {
        try {
            System.out.println("\n-- Listar TODOS --");
            var lista = prestamoService.listarPrestamos();
            imprimirPrestamos(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarActivos() {
        try {
            System.out.println("\n-- Listar ACTIVOS --");
            var lista = prestamoService.listarPrestamosActivos();
            imprimirPrestamos(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarPorUsuario() {
        try {
            System.out.println("\n-- Listar por USUARIO (RUT) --");
            String rut = leerLinea("RUT: ");
            var lista = prestamoService.listarPrestamosPorUsuario(rut);
            imprimirPrestamos(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarPorMaterial() {
        try {
            System.out.println("\n-- Listar por MATERIAL (código) --");
            int codMat = leerEnteroPositivo("Código del material: ");
            var lista = prestamoService.listarPrestamosPorMaterial(codMat);
            imprimirPrestamos(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // ACTUALIZACIONES
    // ───────────────────────────────

    private void registrarDevolucion() {
        try {
            System.out.println("\n-- Registrar devolución --");
            int codigoPrestamo = leerEnteroPositivo("Código del préstamo: ");
            LocalDate fechaDev = leerFechaONull("Fecha de devolución (YYYY-MM-DD, ENTER para hoy): ");
            boolean ok = prestamoService.registrarDevolucion(codigoPrestamo, fechaDev);
            System.out.println(ok ? "Devolución registrada." : "No se pudo registrar la devolución.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void cambiarEstado() {
        try {
            System.out.println("\n-- Cambiar estado --");
            int codigo = leerEnteroPositivo("Código del préstamo: ");
            EstadoPrestamo est = leerEstado();
            boolean ok = prestamoService.cambiarEstado(codigo, est);
            System.out.println(ok ? "Estado actualizado." : "No se pudo actualizar el estado.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void actualizarMontos() {
        try {
            System.out.println("\n-- Actualizar montos --");
            int codigo = leerEnteroPositivo("Código del préstamo: ");
            int dias = leerEnteroNoNegativo("Días de atraso (>=0): ");
            double multa = leerDoubleNoNegativo("Multa aplicada (>=0): ");
            boolean ok = prestamoService.actualizarMontos(codigo, dias, multa);
            System.out.println(ok ? "Montos actualizados." : "No se pudieron actualizar los montos.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // ELIMINACIÓN
    // ───────────────────────────────

    private void eliminarPrestamo() {
        try {
            System.out.println("\n-- Eliminar préstamo --");
            int codigo = leerEnteroPositivo("Código del préstamo: ");
            String conf = leerLinea("Confirmar eliminación (S/N): ");
            if (!conf.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }
            boolean ok = prestamoService.eliminarPrestamo(codigo);
            System.out.println(ok ? "Préstamo eliminado." : "No se pudo eliminar (puede no existir).");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // UTILIDADES
    // ───────────────────────────────

    private void imprimirPrestamos(List<PrestamoDTO> lista) {
        if (lista.isEmpty()) {
            System.out.println("(Sin préstamos)");
            return;
        }
        for (PrestamoDTO p : lista) {
            System.out.printf("- [%d] mat=%d | rut=%s | prest=%s | dev=%s | est=%s | atraso=%d | multa=%.2f%n",
                    p.getCodigo(),
                    p.getCodigoMaterial(),
                    p.getRutUsuario(),
                    optFecha(p.getFechaPrestamo()),
                    optFecha(p.getFechaDevolucion()),
                    (p.getEstado() == null ? "-" : p.getEstado().name()),
                    p.getDiasAtraso(),
                    p.getMultaAplicada());
        }
    }

    private String optFecha(LocalDate f) { return f == null ? "-" : f.toString(); }

    private String leerLinea(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private int leerEnteroPositivo(String prompt) {
        while (true) {
            int v = leerEntero(prompt);
            if (v > 0) return v;
            System.out.println("Debe ser un entero positivo.");
        }
    }

    private int leerEnteroNoNegativo(String prompt) {
        while (true) {
            int v = leerEntero(prompt);
            if (v >= 0) return v;
            System.out.println("Debe ser un entero >= 0.");
        }
    }

    private double leerDoubleNoNegativo(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double v = Double.parseDouble(scanner.nextLine().trim());
                if (v >= 0) return v;
                System.out.println("Debe ser un número >= 0.");
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private LocalDate leerFechaONull(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException ex) {
            System.out.println("Fecha inválida. Se usará la fecha de hoy.");
            return null;
        }
    }

    private EstadoPrestamo leerEstado() {
        while (true) {
            System.out.println("Estados: 1) ACTIVO  2) DEVUELTO");
            int op = leerEntero("Seleccione estado: ");
            if (op == 1) return EstadoPrestamo.ACTIVO;
            if (op == 2) return EstadoPrestamo.DEVUELTO;
            System.out.println("Opción inválida.");
        }
    }

    private void mostrarError(Exception e) {
        String msg = e.getMessage();
        System.out.println("Error: " + (msg == null ? e.getClass().getSimpleName() : msg));
    }
}
