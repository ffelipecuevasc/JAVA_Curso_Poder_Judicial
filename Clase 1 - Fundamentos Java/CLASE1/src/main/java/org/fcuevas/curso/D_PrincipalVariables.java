package org.fcuevas.curso;

public class D_PrincipalVariables {

    public static void main(String[] args) {
        // String: texto inmutable
        String nombre = "Ana";
        String saludo = "Hola, " + nombre + "!";
        System.out.println(saludo);

        // var: inferencia de tipo SOLO para variables locales (equivale al tipo real a la derecha)
        var edad = 30;              // int
        var altura = 1.64;          // double
        var esMayor = edad >= 18;   // boolean

        // printf / format: plantillas con especificadores
        System.out.printf("Nombre: %s | Edad: %d | Altura: %.2f | Mayor de edad: %b%n",
                nombre, edad, altura, esMayor);

        // String.format devuelve un String formateado (no imprime)
        String ficha = String.format("Usuario[%s-%d]", nombre, edad);
        System.out.println(ficha);

        // Algunos especificadores útiles:
        // %s texto, %d entero, %.2f decimal con 2 decimales, %b booleano, %n salto de línea portable
    }
}