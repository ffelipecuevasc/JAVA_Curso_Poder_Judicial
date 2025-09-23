package org.fcuevas.proyecto;

import java.util.*;

/**
 * CLASE 1 - Menú inicial de Casos Judiciales (sin POO todavía).
 * - Estructuras simples: arreglos unidimensionales
 * - Opciones: crear, listar, buscar por ROL
 */
public class PrincipalProyecto {

    // Capacidad fija del "almacén" de casos
    private static final int CAPACIDAD = 5;

    // Arreglos que representan columnas del "registro" de casos
    private static final String[] ROLES = new String[CAPACIDAD];
    private static final String[] CARATULADOS = new String[CAPACIDAD];
    private static final String[] FECHAS = new String[CAPACIDAD];
    private static final String[] ESTADOS = new String[CAPACIDAD];

    // Contador de registros usados (siguiente índice libre)
    private static int contador = 0;

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarBanner();

        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            String opcion = leerLinea("Elige una opción: ");

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
    }

    // ---------------- UI ----------------

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

    // ---------------- Operaciones ----------------

    /**
     * Crea un nuevo caso leyendo datos por consola y almacenándolos en los arrays.
     * Si la capacidad está llena, informa y cancela la creación.
     */
    private static void crearCaso() {
        System.out.println("\n[CREAR CASO]");

        if (contador >= CAPACIDAD) {
            System.out.printf("[ERROR] Capacidad máxima alcanzada (%d casos). No se pueden agregar más casos.%n", CAPACIDAD);
            return;
        }

        String rol = leerNoVacio("Ingrese ROL (ej.: RIT-1234-2025): ");
        String caratulado = leerNoVacio("Ingrese caratulado (ej.: 'Pérez c/ Estado'): ");
        String fechaIngreso = leerNoVacio("Ingrese fecha de ingreso (texto por ahora, ej.: 20-09-2025): ");
        String estado = leerNoVacio("Ingrese estado (ABIERTO / EN_TRAMITE / SUSPENDIDO / CERRADO): ");

        // Verificar duplicado por rol
        int idxExist = indiceDeRol(rol);
        if (idxExist >= 0) {
            System.out.printf("[WARN] Ya existe un caso con ROL '%s'. (Mostrando registro existente)%n", rol);
            imprimirFichaCasoAt(idxExist);
            String resp = leerLinea("Deseas crear uno nuevo con el mismo ROL? (s/n): ");
            if (!resp.equalsIgnoreCase("s") && !resp.equalsIgnoreCase("si")) {
                System.out.println("[INFO] Creación cancelada por usuario.");
                return;
            }
            // Si el usuario desea continuar, se permitirá crear duplicado siempre y cuando haya espacio.
        }

        // Guardar en el siguiente índice libre
        ROLES[contador] = rol;
        CARATULADOS[contador] = caratulado;
        FECHAS[contador] = fechaIngreso;
        ESTADOS[contador] = estado;
        contador++;

        System.out.println("\n[INFO] Caso creado correctamente:");
        imprimirFichaCasoAt(contador - 1);
    }

    /**
     * Lista todos los casos actualmente cargados.
     */
    private static void listarCasos() {
        System.out.println("\n[LISTAR CASOS]");
        if (contador == 0) {
            System.out.println("[INFO] No hay casos cargados.");
            return;
        }
        for (int i = 0; i < contador; i++) {
            imprimirFichaCasoAt(i);
        }
    }

    /**
     * Busca un caso por ROL (ignorando mayúsculas/minúsculas) y lo muestra si existe.
     */
    private static void buscarPorRol() {
        System.out.println("\n[BUSCAR POR ROL]");
        String rolBuscado = leerNoVacio("Ingrese el ROL a buscar: ");

        int idx = indiceDeRol(rolBuscado);
        if (idx >= 0) {
            System.out.println("[INFO] Caso encontrado:");
            imprimirFichaCasoAt(idx);
        } else {
            System.out.println("[INFO] No se encontró un caso con ese ROL.");
        }
    }

    // ---------------- Utilidades de arreglos ----------------

    /**
     * Encuentra el índice del primer caso cuyo rol coincide (ignora mayúsculas).
     * Devuelve -1 si no existe.
     */
    private static int indiceDeRol(String rol) {
        String r = rol == null ? "" : rol.trim();
        for (int i = 0; i < contador; i++) {
            String exist = ROLES[i];
            if (exist != null && exist.equalsIgnoreCase(r)) {
                return i;
            }
        }
        return -1;
    }

    // ---------------- Utilidades de entrada / impresión ----------------

    private static String leerLinea(String cadena) {
        System.out.print(cadena);
        return SC.nextLine();
    }

    private static String leerNoVacio(String cadena) {
        while (true) {
            String v = leerLinea(cadena).trim();
            if (!v.isBlank()) {
                return v;
            }
            System.out.println("[ERROR] El valor no puede estar vacío. Intente nuevamente.\n");
        }
    }

    private static void imprimirFichaCasoAt(int index) {
        if (index < 0 || index >= contador) return;
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
                index + 1,
                safeGet(ROLES, index),
                safeGet(CARATULADOS, index),
                safeGet(FECHAS, index),
                safeGet(ESTADOS, index)
        );
    }

    private static String safeGet(String[] arr, int idx) {
        if (arr == null || idx < 0 || idx >= arr.length) return "";
        String v = arr[idx];
        return v == null ? "" : v;
    }
}