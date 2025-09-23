package org.fcuevas.curso;

public class D_PrincipalVariables {

    public static void main(String[] args) {
        // String: texto inmutable
        String nombre = "Ana";
        String saludo = "Hola, " + nombre + "!";
        System.out.println(saludo);

        //Desde JAVA 10 se pueden definir variables con VAR, no aguanta arreglos.
        //Valores numéricos definidos con VAR son integer por defecto.
        var valor1 = "Felipe Cuevas";
        var valor2 = 10;

        System.out.println("El valor1 es: " + valor1 + " | El valor2 es:" + valor2);
        System.out.println("--------");

        int valorInt = 33000;
        System.out.println("Valor del INT: " + valorInt);
        System.out.println("Tipo INT corresponde en bytes a: " + Integer.BYTES);
        System.out.println("--------");

        //En valores con decimales, la E significa que el valor se le agrega un EXPONENTE seguido del
        //número de veces que será multiplicado por 10, provocando que la , se corra esa cantidad
        float valorFloat = 1.5E4F;
        System.out.println("Valor del FLOAT: " + valorFloat);
        System.out.println("Tipo FLOAT corresponde en bytes a: " + Float.BYTES);
        System.out.println("--------");

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
        // - %s texto
        // - %d entero
        // - %.2f decimal con 2 decimales
        // - %b booleano
        // - %n salto de línea portable
    }
}