package org.fcuevas.proyecto;

import java.util.*;

/**
 * CLASE 1 - Menú inicial de Casos Judiciales (sin POO todavía).
 * - Estructuras simples: List<Map<String,Object>>
 * - Opciones: crear, listar, buscar por ROL
 */
public class ProyectoIntegral {

    // Lista de "casos" en memoria. Cada caso es un Map con claves: rol, caratulado, fechaIngreso, estado
    private static final List<Map<String, Object>> CASOS = new ArrayList<>();
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarBanner();

        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            String opcion = leerLinea("Elige una opción: ");

            // switch expression (Java 17) para decidir acción
            seguir = switch (opcion) {
                case "1" -> {
                    crearCaso();
                    yield true;
                }
                case "2" -> {
                    listarCasos();
                    yield true;
                }
                case "3" -> {
                    buscarPorRol();
                    yield true;
                }
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

        // Cierra scanner si quieres finalizar el proceso (en apps cortas no es crítico)
        // SC.close();
    }

    // ---- UI ----

    private static void mostrarBanner() {
        String banner = """
                ==========================================
                  API Casos Judiciales (consola - CLASE 1)
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
                0) Salir
                -------------------------------
                """;
        System.out.println(menu);
    }

    // ---- Operaciones ----

    private static void crearCaso() {
        System.out.println("\n[CREAR CASO]");

        String rol = leerNoVacio("Ingrese ROL (ej.: RIT-1234-2025): ");
        String caratulado = leerNoVacio("Ingrese caratulado (ej.: 'Pérez c/ Estado'): ");
        String fechaIngreso = leerNoVacio("Ingrese fecha de ingreso (texto por ahora, ej.: 20-09-2025): ");
        String estado = leerNoVacio("Ingrese estado (ABIERTO / EN_TRAMITE / SUSPENDIDO / CERRADO): ");

        Map<String, Object> caso = new HashMap<>();
        caso.put("rol", rol);
        caso.put("caratulado", caratulado);
        caso.put("fechaIngreso", fechaIngreso);
        caso.put("estado", estado);

        CASOS.add(caso);

        System.out.println("\n[INFO] Caso creado correctamente:");
        imprimirFichaCaso(caso);
    }

    private static void listarCasos() {
        System.out.println("\n[LISTAR CASOS]");
        if (CASOS.isEmpty()) {
            System.out.println("[INFO] No hay casos cargados.");
            return;
        }
        for (Map<String, Object> caso : CASOS) {
            imprimirFichaCaso(caso);
        }
    }

    private static void buscarPorRol() {
        System.out.println("\n[BUSCAR POR ROL]");
        String rolBuscado = leerNoVacio("Ingrese el ROL a buscar: ");

        Optional<Map<String, Object>> encontrado = CASOS.stream()
                .filter(c -> rolBuscado.equalsIgnoreCase(String.valueOf(c.get("rol"))))
                .findFirst();

        if (encontrado.isPresent()) {
            System.out.println("[INFO] Caso encontrado:");
            imprimirFichaCaso(encontrado.get());
        } else {
            System.out.println("[INFO] No se encontró un caso con ese ROL.");
        }
    }

    // ---- Utilidades ----

    private static String leerLinea(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }

    private static String leerNoVacio(String prompt) {
        while (true) {
            String v = leerLinea(prompt).trim();
            if (!v.isBlank()) {
                return v;
            }
            System.out.println("[ERROR] El valor no puede estar vacío. Intente nuevamente.\n");
        }
    }

    private static void imprimirFichaCaso(Map<String, Object> caso) {
        String ficha = """
                ====== CASO ======
                ROL: %s
                Caratulado: %s
                Fecha de ingreso: %s
                Estado: %s
                ==================
                """;
        System.out.printf(
                ficha,
                caso.getOrDefault("rol", ""),
                caso.getOrDefault("caratulado", ""),
                caso.getOrDefault("fechaIngreso", ""),
                caso.getOrDefault("estado", "")
        );
    }
}