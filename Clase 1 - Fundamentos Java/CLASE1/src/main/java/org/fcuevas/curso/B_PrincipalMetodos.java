package org.fcuevas.curso;

public class B_PrincipalMetodos {

    // Punto de partida del programa
    public static void main(String[] args) {
        System.out.println("Hola desde main!");
        saludar("Felipe");
    }

    // Metodo estático auxiliar (no hay clases extra necesarias para usarlo aquí).
    private static void saludar(String nombre) {
        System.out.println("Hola, " + nombre + ".");
    }
}