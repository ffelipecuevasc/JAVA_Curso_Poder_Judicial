package org.fcuevas.curso;

import java.util.Scanner;

public class C_PrincipalNormalizacion {
    static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        String nombre = leerNoVacio("Nombre: ");
        int edad = leerEntero("Edad (entero): ");
        boolean confirmado = leerSiNo("¿Confirmas? (s/n): ");
        System.out.printf("OK: nombre=%s, edad=%d, confirmado=%b%n", nombre, edad, confirmado);
    }

    // Repite hasta que el usuario escriba algo no vacío
    static String leerNoVacio(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = SC.nextLine().trim();
            if (!v.isBlank()) return v;
            System.out.println("[ERROR] No puede estar vacío. Intenta nuevamente.");
        }
    }

    // Lee un entero con manejo de error; reintenta si hay formato inválido
    static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = SC.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Debe ser un número entero. Intenta nuevamente.");
            }
        }
    }

    // Interpreta 's', 'si', 'y', 'yes' como true; 'n', 'no' como false; reintenta si no entiende
    static boolean leerSiNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String v = SC.nextLine().trim().toLowerCase();
            if (v.equals("s") || v.equals("si") || v.equals("y") || v.equals("yes")) return true;
            if (v.equals("n") || v.equals("no")) return false;
            System.out.println("[ERROR] Responde con s/n (sí/no).");
        }
    }
}
