package org.fcuevas.proyecto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public class PrincipalProyecto {

    // ---- Config general ----
    private static final Locale LOCALE = Locale.forLanguageTag("es-CL");
    private static final DateTimeFormatter FMT_FECHA_STRICT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu", LOCALE).withResolverStyle(ResolverStyle.STRICT);

    // ---- Capacidad FIJA (5) según lo pedido ----
    private static final int CAPACITY = 5;
    private static final String[] ROLES = new String[CAPACITY];
    private static final String[] CARATULADOS = new String[CAPACITY];
    private static final String[] FECHAS = new String[CAPACITY];   // almacenamos como String (validado)
    private static final String[] ESTADOS = new String[CAPACITY];
    private static int count = 0;

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        Locale.setDefault(LOCALE);
        mostrarBanner();

        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            String opcion = leerLinea("Elige una opción: ").trim();
            seguir = switch (opcion) {
                case "1" -> { crearCaso(); yield true; }
                case "2" -> { listarCasos(); yield true; }
                case "3" -> { buscarPorRol(); yield true; }
                case "4" -> { exportarCsv(); yield true; } // try-with-resources (preview)
                case "0" -> {
                    System.out.println("\nSaliendo... ¡Gracias por usar el sistema!");
                    yield false;
                }
                default -> {
                    System.out.println("[ERROR] Opción inválida. Intenta nuevamente.");
                    yield true;
                }
            };
        }
        // Nota: No cerramos SC porque cierra System.in (innecesario aquí y puede interferir en otras lecturas).
    }

    // ===================== UI =====================

    private static void mostrarBanner() {
        String banner = """
                ==========================================
                  API Casos Judiciales (Consola) — CLASE 2
                ==========================================
                """;
        System.out.println(banner);
    }

    private static void mostrarMenu() {
        String menu = """
                -------- MENÚ PRINCIPAL --------
                1) Crear caso
                2) Listar casos
                3) Buscar caso por ROL
                4) Exportar a CSV (try-with-resources)
                0) Salir
                -------------------------------
                """;
        System.out.println(menu);
    }

    // ================= Operaciones CRUD =================

    private static void crearCaso() {
        System.out.println("\n[CREAR CASO]");

        if (count >= CAPACITY) {
            System.out.printf("[ERROR] Capacidad máxima alcanzada (%d casos).%n", CAPACITY);
            return;
        }

        try {
            String rol = leerNoVacio("ROL (formato ej.: RIT-123-2025): ");
            if (!esRolValido(rol)) throw new IllegalArgumentException("Formato de ROL inválido (use RIT-###-AAAA).");
            rol = normalizaRol(rol);

            String caratulado = leerNoVacio("Caratulado: ");

            String fechaTxt = leerNoVacio("Fecha de ingreso (dd-MM-yyyy): ");
            // Validación estricta de calendario con LocalDate
            LocalDate fechaValida = parseFechaStrict(fechaTxt);
            String fechaAlmacenada = fechaValida.format(FMT_FECHA_STRICT); // normalizamos formato

            String estado = leerNoVacio("Estado (ABIERTO / EN_TRAMITE / SUSPENDIDO / CERRADO): ").toUpperCase();

            // Duplicados por ROL: advertimos y pedimos confirmación
            Optional<Integer> idxDup = buscarIndexPorRol(rol);
            if (idxDup.isPresent()) {
                imprimirFichaCaso(idxDup.get());
                String resp = leerLinea("Ya existe ese ROL. ¿Crear de todas formas? (s/n): ").trim().toLowerCase();
                if (!(resp.equals("s") || resp.equals("si"))) {
                    System.out.println("[INFO] Creación cancelada por el usuario.");
                    return;
                }
            }

            // Guardar (capacidad garantizada arriba)
            ROLES[count] = rol;
            CARATULADOS[count] = caratulado;
            FECHAS[count] = fechaAlmacenada;
            ESTADOS[count] = estado;
            count++;

            System.out.println("[INFO] Caso agregado correctamente:");
            imprimirFichaCaso(count - 1);

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void listarCasos() {
        System.out.println("\n[LISTAR CASOS]");
        if (count == 0) {
            System.out.println("[INFO] No hay casos cargados.");
            return;
        }
        for (int i = 0; i < count; i++) {
            imprimirFichaCaso(i);
        }
    }

    private static void buscarPorRol() {
        System.out.println("\n[BUSCAR POR ROL]");
        String rolBuscado = normalizaRol(leerNoVacio("Ingrese el ROL a buscar: "));
        Optional<Integer> idx = buscarIndexPorRol(rolBuscado);
        idx.ifPresentOrElse(
                PrincipalProyecto::imprimirFichaCaso,
                () -> System.out.println("[INFO] No se encontró un caso con ese ROL.")
        );
    }

    // ============== Exportación (try-with-resources) ==============

    private static void exportarCsv() {
        System.out.println("\n[EXPORTAR CSV]");
        if (count == 0) {
            System.out.println("[INFO] No hay datos que exportar.");
            return;
        }
        String nombre = leerNoVacio("Nombre de archivo (ej.: casos.csv): ");
        Path path = Path.of(nombre);

        // try-with-resources: el BufferedWriter se cierra SOLO, incluso si hay excepción
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("rol,caratulado,fechaIngreso,estado");
            bw.newLine();
            for (int i = 0; i < count; i++) {
                String linea = "%s,%s,%s,%s".formatted(
                        safe(ROLES[i]), safe(CARATULADOS[i]), safe(FECHAS[i]), safe(ESTADOS[i])
                );
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("[INFO] Exportación completada → " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("[I/O ERROR] No se pudo exportar: " + e.getMessage());
        }
    }

    // ================ Utilidades de búsqueda/validación ================

    private static Optional<Integer> buscarIndexPorRol(String rolNormalizado) {
        for (int i = 0; i < count; i++) {
            if (rolNormalizado.equalsIgnoreCase(ROLES[i])) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    // Formato simple “RIT-<3..5 dígitos>-<4 dígitos>”
    private static boolean esRolValido(String rol) {
        return rol != null && rol.trim().toUpperCase().matches("RIT-\\d{3,5}-\\d{4}");
    }

    private static String normalizaRol(String rol) {
        return rol.trim().toUpperCase();
    }

    private static LocalDate parseFechaStrict(String fechaTxt) {
        try {
            return LocalDate.parse(fechaTxt.trim(), FMT_FECHA_STRICT);
        } catch (Exception ex) {
            // Mostramos un mensaje entendible; propagamos como unchecked para simplificar el flujo del menú
            throw new IllegalArgumentException("Fecha inválida. Use formato dd-MM-yyyy y una fecha real.");
        }
    }

    // ==================== I/O / helpers ====================

    private static String leerLinea(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }

    private static String leerNoVacio(String prompt) {
        while (true) {
            String v = leerLinea(prompt).trim();
            if (!v.isBlank()) return v;
            System.out.println("[ERROR] El valor no puede estar vacío. Inténtelo nuevamente.\n");
        }
    }

    private static void imprimirFichaCaso(int idx) {
        if (idx < 0 || idx >= count) return;
        String ficha = """
                ====== CASO #%d ======
                ROL: %s
                Caratulado: %s
                Fecha de ingreso: %s
                Estado: %s
                ======================
                """;
        System.out.printf(
                ficha,
                idx + 1,
                ROLES[idx],
                CARATULADOS[idx],
                FECHAS[idx],
                ESTADOS[idx]
        );
    }

    private static String safe(String v) { return v == null ? "" : v; }
}