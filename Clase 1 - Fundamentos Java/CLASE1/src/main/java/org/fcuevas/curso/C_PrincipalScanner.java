package org.fcuevas.curso;

import java.util.Scanner;

public class C_PrincipalScanner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // en programas cortos no es grave no cerrarlo
        boolean seguir = true;
        while (seguir) {
            System.out.println("""
                    === MENÚ ===
                    1) Sumar dos números
                    2) Eco de texto
                    0) Salir
                    """);
            System.out.print("Opción: ");
            // Leer el contenido como texto evita problemas de salto de línea
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    System.out.print("a = ");
                    int a = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("b = ");
                    int b = Integer.parseInt(sc.nextLine().trim());
                    System.out.println("Resultado: " + (a + b));
                }
                case "2" -> {
                    System.out.print("Escribe algo: ");
                    String t = sc.nextLine();
                    System.out.println("Eco: " + t);
                }
                case "0" -> {
                    System.out.println("Chao!");
                    seguir = false;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}