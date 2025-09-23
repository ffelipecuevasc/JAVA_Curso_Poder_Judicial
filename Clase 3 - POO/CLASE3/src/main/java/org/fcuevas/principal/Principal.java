package org.fcuevas.principal;

import java.time.LocalDate;
import java.util.*;
import org.fcuevas.clases.*;
import org.fcuevas.enums.*;

public class Principal {

    private static final Scanner SC = new Scanner(System.in);

    // "Base de datos" en memoria
    private static final List<Caso> casos = new ArrayList<>();
    private static final Map<String, List<Parte>> partesPorRol = new HashMap<>();

    public static void main(String[] args) {
        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            String op = leer("Opción: ");
            switch (op) {
                case "1" -> crearCaso();
                case "2" -> listarCasos();
                case "3" -> buscarCasoPorRol();
                case "4" -> cambiarEstadoCaso();
                case "5" -> agregarParteACaso();
                case "6" -> listarPartesDeCaso();
                case "7" -> agregarRepresentanteAParte();
                case "8" -> listarRepresentantesDeParte();
                case "9" -> gestionarMandatoRepresentante();
                case "0" -> {
                    System.out.println("Saliendo... ¡Gracias!");
                    seguir = false;
                }
                default -> System.out.println("Opción inválida.");
            }
            System.out.println();
        }
        SC.close();
    }

    // -------------------- Menú --------------------

    private static void mostrarMenu() {
        System.out.println("""
                ================= MENÚ =================
                1) Crear caso
                2) Listar casos
                3) Buscar caso por ROL
                4) Cambiar estado de un caso
                5) Agregar Parte a un caso
                6) Listar Partes de un caso
                7) Agregar Representante a una Parte
                8) Listar Representantes de una Parte
                9) Activar/Revocar mandato de un Representante
                0) Salir
                =========================================
                """);
    }

    private static void crearCaso() {
        System.out.println("[Crear caso]");
        String rol = leer("ROL: ");
        String caratulado = leer("Caratulado: ");
        String fechaTxt = leer("Fecha de ingreso (formato ISO: yyyy-MM-dd): ");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTxt);
        } catch (Exception e) {
            System.out.println("Fecha inválida. Operación cancelada.");
            return;
        }

        String estadoTxt = leer("Estado (ABIERTO/EN_TRAMITE/SUSPENDIDO/CERRADO) [Enter=ABIERTO]: ");
        EstadoCaso estado = EstadoCaso.ABIERTO;
        if (!estadoTxt.isBlank()) {
            try {
                estado = EstadoCaso.valueOf(estadoTxt);
            } catch (Exception e) {
                System.out.println("Estado no reconocido. Se usará ABIERTO.");
                estado = EstadoCaso.ABIERTO;
            }
        }

        try {
            Caso c = new Caso(rol, caratulado, fecha, estado);
            casos.add(c);
            partesPorRol.put(rol, new ArrayList<>());
            System.out.println("Caso creado.");
        } catch (Exception e) {
            System.out.println("No se pudo crear el caso: " + e.getMessage());
        }
    }

    private static void listarCasos() {
        System.out.println("[Listar casos]");
        if (casos.isEmpty()) {
            System.out.println("No hay casos.");
            return;
        }
        for (Caso c : casos) {
            System.out.println(c.imprimirResumen());
        }
    }

    private static void buscarCasoPorRol() {
        System.out.println("[Buscar caso]");
        String rol = leer("ROL: ");
        Caso c = encontrarCaso(rol);
        if (c == null) {
            System.out.println("No existe un caso con ese ROL.");
        } else {
            System.out.println(c.imprimirResumen());
        }
    }

    private static void cambiarEstadoCaso() {
        System.out.println("[Cambiar estado]");
        String rol = leer("ROL: ");
        Caso c = encontrarCaso(rol);
        if (c == null) {
            System.out.println("No existe un caso con ese ROL.");
            return;
        }
        String estadoTxt = leer("Nuevo estado (ABIERTO/EN_TRAMITE/SUSPENDIDO/CERRADO): ");
        try {
            EstadoCaso nuevo = EstadoCaso.valueOf(estadoTxt);
            c.cambiarEstado(nuevo);
            System.out.println("Estado actualizado.");
        } catch (Exception e) {
            System.out.println("No se pudo cambiar el estado: " + e.getMessage());
        }
    }

    private static void agregarParteACaso() {
        System.out.println("[Agregar Parte]");
        String rol = leer("ROL del caso: ");
        Caso c = encontrarCaso(rol);
        if (c == null) {
            System.out.println("No existe un caso con ese ROL.");
            return;
        }

        String run = leer("RUN de la Parte: ");
        String nombres = leer("Nombres: ");
        String apellidos = leer("Apellidos: ");
        String rolParteTxt = leer("Rol en causa (DEMANDANTE/DEMANDADO/QUERELLANTE/IMPUTADO/RECURRENTE/RECURRIDO): ");

        RolEnCausa rec;
        try {
            rec = RolEnCausa.valueOf(rolParteTxt);
        } catch (Exception e) {
            System.out.println("Rol en causa no reconocido. Operación cancelada.");
            return;
        }

        try {
            Parte p = new Parte(run, nombres, apellidos, rec);
            partesPorRol.computeIfAbsent(rol, k -> new ArrayList<>()).add(p);
            System.out.println("Parte agregada al caso.");
        } catch (Exception e) {
            System.out.println("No se pudo agregar la Parte: " + e.getMessage());
        }
    }

    private static void listarPartesDeCaso() {
        System.out.println("[Listar Partes]");
        String rol = leer("ROL del caso: ");
        List<Parte> partes = partesPorRol.get(rol);
        if (partes == null || partes.isEmpty()) {
            System.out.println("No hay partes registradas para ese caso.");
            return;
        }
        for (Parte p : partes) {
            System.out.printf("- %s %s | RUN: %s | Rol: %s | Representantes: %d%n",
                    p.getNombres(), p.getApellidos(), p.getRun(), p.getRolEnCausa(), p.getRepresentantes().size());
        }
    }

    private static void agregarRepresentanteAParte() {
        System.out.println("[Agregar Representante]");
        String rol = leer("ROL del caso: ");
        List<Parte> partes = partesPorRol.get(rol);
        if (partes == null || partes.isEmpty()) {
            System.out.println("No hay partes para ese caso.");
            return;
        }

        String runParte = leer("RUN de la Parte: ");
        Parte parte = buscarPartePorRun(partes, runParte);
        if (parte == null) {
            System.out.println("No se encontró la Parte en ese caso.");
            return;
        }

        String runRep = leer("RUN del Representante: ");
        String nombres = leer("Nombres: ");
        String apellidos = leer("Apellidos: ");
        String tipoTxt = leer("Tipo (ABOGADO/PROCURADOR/APODERADO/DEFENSOR_PUBLICO): ");
        String institucion = leer("Institución (opcional, Enter para omitir): ");

        TipoRepresentante tipo;
        try {
            tipo = TipoRepresentante.valueOf(tipoTxt);
        } catch (Exception e) {
            System.out.println("Tipo no reconocido. Operación cancelada.");
            return;
        }

        try {
            Representante r = new Representante(runRep, nombres, apellidos, tipo);
            if (!institucion.isBlank()) r.setInstitucion(institucion);

            // Activar mandato si el usuario lo desea
            String activar = leer("¿Activar mandato? (s/n): ");
            if (activar.equalsIgnoreCase("s")) {
                r.setMandatoVigente();
            }

            parte.agregarRepresentante(r);
            System.out.println("Representante agregado.");
        } catch (Exception e) {
            System.out.println("No se pudo agregar el representante: " + e.getMessage());
        }
    }

    private static void listarRepresentantesDeParte() {
        System.out.println("[Listar Representantes]");
        String rol = leer("ROL del caso: ");
        List<Parte> partes = partesPorRol.get(rol);
        if (partes == null || partes.isEmpty()) {
            System.out.println("No hay partes para ese caso.");
            return;
        }

        String runParte = leer("RUN de la Parte: ");
        Parte parte = buscarPartePorRun(partes, runParte);
        if (parte == null) {
            System.out.println("No se encontró la Parte en ese caso.");
            return;
        }

        if (parte.getRepresentantes().isEmpty()) {
            System.out.println("La Parte no tiene representantes.");
            return;
        }

        for (Representante rep : parte.getRepresentantes()) {
            System.out.printf("- %s %s | RUN: %s | Tipo: %s | Vigente: %b | Institución: %s%n",
                    rep.getNombres(), rep.getApellidos(), rep.getRun(),
                    rep.getTipo(), rep.getMandatoVigencia(), rep.getInstitucion());
        }
    }

    private static void gestionarMandatoRepresentante() {
        System.out.println("[Activar/Revocar mandato]");
        String rol = leer("ROL del caso: ");
        List<Parte> partes = partesPorRol.get(rol);
        if (partes == null || partes.isEmpty()) {
            System.out.println("No hay partes para ese caso.");
            return;
        }

        String runParte = leer("RUN de la Parte: ");
        Parte parte = buscarPartePorRun(partes, runParte);
        if (parte == null) {
            System.out.println("No se encontró la Parte en ese caso.");
            return;
        }

        String runRep = leer("RUN del Representante: ");
        Optional<Representante> repOpt = parte.buscarRepresentantePorRun(runRep);
        if (repOpt.isEmpty()) {
            System.out.println("No se encontró el representante.");
            return;
        }
        Representante rep = repOpt.get();

        String accion = leer("Acción (A=activar, R=revocar): ");
        if (accion.equalsIgnoreCase("A")) {
            rep.setMandatoVigente();
            System.out.println("Mandato ACTIVADO.");
        } else if (accion.equalsIgnoreCase("R")) {
            rep.revocarMandatoVigente();
            System.out.println("Mandato REVOCADO.");
        } else {
            System.out.println("Acción no reconocida.");
        }
    }

    private static String leer(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }

    private static Caso encontrarCaso(String rol) {
        for (Caso c : casos) {
            if (Objects.equals(c.getRol(), rol)) return c;
        }
        return null;
    }

    private static Parte buscarPartePorRun(List<Parte> partes, String run) {
        for (Parte p : partes) {
            if (Objects.equals(p.getRun(), run)) return p;
        }
        return null;
    }
}