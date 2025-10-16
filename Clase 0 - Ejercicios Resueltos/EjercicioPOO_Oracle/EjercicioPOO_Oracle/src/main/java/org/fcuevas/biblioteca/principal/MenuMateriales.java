package org.fcuevas.biblioteca.principal;

import org.fcuevas.biblioteca.dto.*;
import org.fcuevas.biblioteca.enums.TipoMaterial;
import org.fcuevas.biblioteca.service.interfaces.IMaterialService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuMateriales {

    private IMaterialService materialService;
    private Scanner scanner = new Scanner(System.in);

    public MenuMateriales(IMaterialService materialService) {
        this.materialService = materialService;
    }

    /** Bucle principal del submenú de Materiales. */
    public void ejecutar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1  -> crearLibro();
                case 2  -> crearRevista();
                case 3  -> crearAudiolibro();
                case 4  -> listarTodos();
                case 5  -> listarPorTipo();
                case 6  -> listarDisponiblesPorTipo();
                case 7  -> buscarPorCodigo();
                case 8  -> actualizarMaterial();
                case 9  -> actualizarDetalleLibro();
                case 10 -> actualizarDetalleRevista();
                case 11 -> actualizarDetalleAudiolibro();
                case 12 -> cambiarDisponibilidad();
                case 13 -> eliminarMaterial();
                case 14 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 14);
    }

    private void mostrarOpciones() {
        System.out.println("\n=== Gestión de Materiales ===");
        System.out.println("1)  Crear LIBRO");
        System.out.println("2)  Crear REVISTA");
        System.out.println("3)  Crear AUDIOLIBRO");
        System.out.println("4)  Listar TODOS los materiales");
        System.out.println("5)  Listar por TIPO");
        System.out.println("6)  Listar DISPONIBLES por TIPO");
        System.out.println("7)  Buscar por CÓDIGO");
        System.out.println("8)  Actualizar MATERIAL (supertipo)");
        System.out.println("9)  Actualizar detalle LIBRO");
        System.out.println("10) Actualizar detalle REVISTA");
        System.out.println("11) Actualizar detalle AUDIOLIBRO");
        System.out.println("12) Cambiar DISPONIBILIDAD");
        System.out.println("13) Eliminar MATERIAL");
        System.out.println("14) Volver");
    }

    // ───────────────────────────────
    // CREACIÓN
    // ───────────────────────────────

    private void crearLibro() {
        try {
            System.out.println("\n-- Crear LIBRO --");
            String titulo = leerLinea("Título: ");
            int diasBase = leerEnteroPositivo("Días base (>0): ");
            String autor = leerLinea("Autor: ");
            Integer anio = leerEnteroONull("Año (ENTER para omitir): ");
            String isbn = leerLinea("ISBN: ");

            MaterialDTO base = new MaterialDTO(titulo, TipoMaterial.LIBRO, diasBase);
            LibroDTO det = new LibroDTO(null, autor, anio, isbn);
            Integer codigo = materialService.crearLibro(base, det);
            System.out.println("LIBRO creado con código: " + codigo);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void crearRevista() {
        try {
            System.out.println("\n-- Crear REVISTA --");
            String titulo = leerLinea("Título: ");
            int diasBase = leerEnteroPositivo("Días base (>0): ");
            int numeroEdicion = leerEnteroNoNegativo("Número de edición (>=0): ");
            String periodicidad = leerLinea("Periodicidad (Semanal, Mensual, ...): ");

            MaterialDTO base = new MaterialDTO(titulo, TipoMaterial.REVISTA, diasBase);
            RevistaDTO det = new RevistaDTO(null, numeroEdicion, periodicidad);
            Integer codigo = materialService.crearRevista(base, det);
            System.out.println("REVISTA creada con código: " + codigo);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void crearAudiolibro() {
        try {
            System.out.println("\n-- Crear AUDIOLIBRO --");
            String titulo = leerLinea("Título: ");
            int diasBase = leerEnteroPositivo("Días base (>0): ");
            String narrador = leerLinea("Narrador: ");
            int duracion = leerEnteroPositivo("Duración (minutos >0): ");
            boolean licencia = leerSiNo("¿Tiene licencia digital? (S/N): ");

            MaterialDTO base = new MaterialDTO(titulo, TipoMaterial.AUDIOLIBRO, diasBase);
            AudioLibroDTO det = new AudioLibroDTO(null, narrador, duracion, licencia);
            Integer codigo = materialService.crearAudiolibro(base, det);
            System.out.println("AUDIOLIBRO creado con código: " + codigo);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // CONSULTAS / LISTADOS
    // ───────────────────────────────

    private void listarTodos() {
        try {
            System.out.println("\n-- Listar TODOS --");
            var lista = materialService.listarMateriales();
            imprimirMateriales(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarPorTipo() {
        try {
            System.out.println("\n-- Listar por TIPO --");
            TipoMaterial tipo = leerTipo();
            var lista = materialService.listarPorTipo(tipo);
            imprimirMateriales(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void listarDisponiblesPorTipo() {
        try {
            System.out.println("\n-- Listar DISPONIBLES por TIPO --");
            TipoMaterial tipo = leerTipo();
            var lista = materialService.listarDisponiblesPorTipo(tipo);
            imprimirMateriales(lista);
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void buscarPorCodigo() {
        try {
            System.out.println("\n-- Buscar por CÓDIGO --");
            int codigo = leerEnteroPositivo("Código: ");
            Optional<MaterialDTO> opt = materialService.buscarPorCodigo(codigo);
            if (opt.isEmpty()) {
                System.out.println("No existe material con ese código.");
                return;
            }
            var m = opt.get();
            System.out.printf("Encontrado: [%d] %s | %s | días=%d | disp=%s%n",
                    m.getCodigo(), m.getTitulo(), m.getTipo(), m.getDiasBase(), m.isDisponible());

            // Mostrar detalle según tipo
            switch (m.getTipo()) {
                case LIBRO -> materialService.obtenerDetalleLibro(m.getCodigo())
                        .ifPresent(l -> System.out.printf("  LIBRO: autor=%s | año=%s | isbn=%s%n",
                                l.getAutor(), l.getAño(), l.getIsbn()));
                case REVISTA -> materialService.obtenerDetalleRevista(m.getCodigo())
                        .ifPresent(r -> System.out.printf("  REVISTA: edición=%d | periodicidad=%s%n",
                                r.getNumeroEdicion(), r.getPeriodicidad()));
                case AUDIOLIBRO -> materialService.obtenerDetalleAudioLibro(m.getCodigo())
                        .ifPresent(a -> System.out.printf("  AUDIOLIBRO: narrador=%s | duración=%d | licencia=%s%n",
                                a.getNarrador(), a.getDuracionMin(), a.getLicenciaDigital()));
            }
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // ACTUALIZACIONES
    // ───────────────────────────────

    private void actualizarMaterial() {
        try {
            System.out.println("\n-- Actualizar MATERIAL (supertipo) --");
            int codigo = leerEnteroPositivo("Código: ");
            Optional<MaterialDTO> opt = materialService.buscarPorCodigo(codigo);
            if (opt.isEmpty()) {
                System.out.println("No existe material con ese código.");
                return;
            }
            MaterialDTO actual = opt.get();
            String titulo = leerLineaDef("Nuevo título (ENTER para mantener '" + actual.getTitulo() + "'): ", actual.getTitulo());
            int diasBase = leerEnteroDef("Nuevos días base (actual " + actual.getDiasBase() + "): ", actual.getDiasBase());
            boolean disponible = leerSiNo("¿Disponible? (S/N, actual " + (actual.isDisponible() ? "S" : "N") + "): ");

            // Tipo se mantiene (si quisieras permitir cambio, deberías también migrar detalle de subtipo)
            MaterialDTO mod = new MaterialDTO(actual.getCodigo(), titulo, actual.getTipo(), diasBase, disponible);
            boolean ok = materialService.actualizarMaterial(mod);
            System.out.println(ok ? "Material actualizado." : "No se pudo actualizar.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void actualizarDetalleLibro() {
        try {
            System.out.println("\n-- Actualizar detalle LIBRO --");
            int codigoMaterial = leerEnteroPositivo("Código del material: ");
            Optional<LibroDTO> det = materialService.obtenerDetalleLibro(codigoMaterial);
            if (det.isEmpty()) {
                System.out.println("No hay detalle de LIBRO para ese material.");
                return;
            }
            LibroDTO actual = det.get();
            String autor = leerLineaDef("Nuevo autor (ENTER para '" + actual.getAutor() + "'): ", actual.getAutor());
            Integer anio = leerEnteroONullDef("Nuevo año (ENTER para " + actual.getAño() + "): ", actual.getAño());
            String isbn = leerLineaDef("Nuevo ISBN (ENTER para '" + actual.getIsbn() + "'): ", actual.getIsbn());

            LibroDTO mod = new LibroDTO(codigoMaterial, autor, anio, isbn);
            boolean ok = materialService.actualizarDetalleLibro(mod);
            System.out.println(ok ? "Detalle de LIBRO actualizado." : "No se pudo actualizar.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void actualizarDetalleRevista() {
        try {
            System.out.println("\n-- Actualizar detalle REVISTA --");
            int codigoMaterial = leerEnteroPositivo("Código del material: ");
            Optional<RevistaDTO> det = materialService.obtenerDetalleRevista(codigoMaterial);
            if (det.isEmpty()) {
                System.out.println("No hay detalle de REVISTA para ese material.");
                return;
            }
            RevistaDTO actual = det.get();
            int numeroEd = leerEnteroDef("Nuevo número de edición (actual " + actual.getNumeroEdicion() + "): ", actual.getNumeroEdicion());
            String periodicidad = leerLineaDef("Nueva periodicidad (ENTER para '" + actual.getPeriodicidad() + "'): ",
                    actual.getPeriodicidad());

            RevistaDTO mod = new RevistaDTO(codigoMaterial, numeroEd, periodicidad);
            boolean ok = materialService.actualizarDetalleRevista(mod);
            System.out.println(ok ? "Detalle de REVISTA actualizado." : "No se pudo actualizar.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void actualizarDetalleAudiolibro() {
        try {
            System.out.println("\n-- Actualizar detalle AUDIOLIBRO --");
            int codigoMaterial = leerEnteroPositivo("Código del material: ");
            Optional<AudioLibroDTO> det = materialService.obtenerDetalleAudioLibro(codigoMaterial);
            if (det.isEmpty()) {
                System.out.println("No hay detalle de AUDIOLIBRO para ese material.");
                return;
            }
            AudioLibroDTO actual = det.get();
            String narrador = leerLineaDef("Nuevo narrador (ENTER para '" + actual.getNarrador() + "'): ", actual.getNarrador());
            int duracion = leerEnteroDef("Nueva duración (min) (actual " + actual.getDuracionMin() + "): ", actual.getDuracionMin());
            boolean licencia = leerSiNo("¿Licencia digital? (S/N, actual " + (actual.getLicenciaDigital() ? "S" : "N") + "): ");

            AudioLibroDTO mod = new AudioLibroDTO(codigoMaterial, narrador, duracion, licencia);
            boolean ok = materialService.actualizarDetalleAudioLibro(mod);
            System.out.println(ok ? "Detalle de AUDIOLIBRO actualizado." : "No se pudo actualizar.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void cambiarDisponibilidad() {
        try {
            System.out.println("\n-- Cambiar DISPONIBILIDAD --");
            int codigo = leerEnteroPositivo("Código del material: ");
            boolean disponible = leerSiNo("¿Marcar como disponible? (S/N): ");
            boolean ok = materialService.cambiarDisponibilidad(codigo, disponible);
            System.out.println(ok ? "Disponibilidad actualizada." : "No se pudo actualizar.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    private void eliminarMaterial() {
        try {
            System.out.println("\n-- Eliminar MATERIAL --");
            int codigo = leerEnteroPositivo("Código del material: ");
            String conf = leerLinea("Confirmar eliminación (S/N): ");
            if (!conf.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }
            boolean ok = materialService.eliminarMaterial(codigo);
            System.out.println(ok ? "Material eliminado." : "No se pudo eliminar (puede no existir).");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    // ───────────────────────────────
    // Utilidades de entrada/salida
    // ───────────────────────────────

    private TipoMaterial leerTipo() {
        while (true) {
            System.out.println("Tipos: 1) LIBRO  2) REVISTA  3) AUDIOLIBRO");
            int t = leerEntero("Seleccione tipo: ");
            switch (t) {
                case 1: return TipoMaterial.LIBRO;
                case 2: return TipoMaterial.REVISTA;
                case 3: return TipoMaterial.AUDIOLIBRO;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    private void imprimirMateriales(List<MaterialDTO> lista) {
        if (lista.isEmpty()) {
            System.out.println("(Sin materiales)");
            return;
        }
        for (var m : lista) {
            System.out.printf("- [%d] %s | %s | días=%d | disp=%s%n",
                    m.getCodigo(), m.getTitulo(), m.getTipo(), m.getDiasBase(), m.isDisponible());
        }
    }

    private String leerLinea(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String leerLineaDef(String prompt, String defecto) {
        System.out.print(prompt);
        String s = scanner.nextLine();
        return (s == null || s.trim().isEmpty()) ? defecto : s.trim();
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

    private int leerEnteroDef(String prompt, int defecto) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return defecto;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Se mantiene: " + defecto);
            return defecto;
        }
    }

    private Integer leerEnteroONull(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Se omitirá (null).");
            return null;
        }
    }

    private Integer leerEnteroONullDef(String prompt, Integer defecto) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return defecto;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Se mantiene: " + defecto);
            return defecto;
        }
    }

    private boolean leerSiNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (s.equalsIgnoreCase("S")) return true;
            if (s.equalsIgnoreCase("N")) return false;
            System.out.println("Responda con 'S' o 'N'.");
        }
    }

    private void mostrarError(Exception e) {
        String msg = e.getMessage();
        System.out.println("Error: " + (msg == null ? e.getClass().getSimpleName() : msg));
    }
}
