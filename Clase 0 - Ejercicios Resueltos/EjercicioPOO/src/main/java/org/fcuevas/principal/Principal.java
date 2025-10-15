package org.fcuevas.principal;

import org.fcuevas.estructurales.Material;
import org.fcuevas.utilitarias.BibliotecaOps;

import java.util.Scanner;

public class Principal {
    private static final Scanner SC = new Scanner(System.in);
    private static final BibliotecaOps BIBLIOTECA = new BibliotecaOps();

    public static void main(String[] args) {
        boolean mantenerMenu = true;
        int opcion;
        while(mantenerMenu){
            menú();
            opcion = SC.nextInt();
            switch (opcion){
                case 1 -> agregarMaterial();
                case 2 -> agregarUsuario();
                case 3 -> listarMateriales();
                case 4 -> listarMaterialesDisponibles();
                case 5 -> realizarPrestamo();
                case 6 -> registrarDevolucion();
                case 7 -> listarPrestamosActivos();
                case 8 -> listarMultasPorUsuario();
                case 9 -> salir();
                default -> System.out.println("Opción inválida, reintente.");
            }
        }
    }

    public static void menú(){
        System.out.println("""
                ----------------
                MENÚ APLICACIÓN
                ----------------
                1) Agregar nuevo material.
                2) Agregar nuevo usuario.
                3) Listar todos los materiales.
                4) Listar los materiales disponibles.
                5) Realizar préstamo.
                6) Registrar devolución.
                7) Listar préstamos activos.
                8) Listar multas por usuario.
                9) Salir.
                ----------------
                Ingrese la opción a ejecutar: """);
    }

    public static void agregarMaterial(){
        System.out.println("[OPCIÓN 1] - Agregando un nuevo material.");
        BIBLIOTECA.agregarMaterialInicio();
    }

    public static void agregarUsuario(){
        System.out.println("[OPCIÓN 2] - Agregando un nuevo usuario.");
        BIBLIOTECA.agregarUsuario();
    }

    public static void listarMateriales(){
        System.out.println("[OPCIÓN 3] - Listando todos los materiales existentes.");
        BIBLIOTECA.mostrarMateriales();
    }

    public static void listarMaterialesDisponibles(){
        System.out.println("[OPCIÓN 4] - Listando todos los materiales disponibles.");
        BIBLIOTECA.mostrarMaterialesDisponibles();
    }

    public static void realizarPrestamo(){
        System.out.println("[OPCIÓN 5] - Realizando un préstamo.");
        BIBLIOTECA.realizarPrestamo();
        }

    public static void registrarDevolucion(){

    }

    public static void listarPrestamosActivos(){
        System.out.println("[OPCIÓN 7] - Listando todos los préstamos.");
        BIBLIOTECA.mostrarPrestamoDisponibles();
    }

    public static void listarMultasPorUsuario(){

    }

    public static void salir(){
        System.out.println("[OPCIÓN 9] - Cerrando recursos y finalizando la aplicación.");
        BIBLIOTECA.cerrarRecursosBiblioteca();
        System.exit(0);
    }
}