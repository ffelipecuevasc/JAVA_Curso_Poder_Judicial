package org.fcuevas.curso;

public class F_PrincipalTryCatch {
    public static void main(String[] args) {
        String[] datos = {"10", "x", null};

        for (String s : datos) {
            try {
                int n = parseEntero(s);
                System.out.println("OK: " + n);
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("[ERROR] Entrada inválida: " + s);
            } finally {
                System.out.println("Intento finalizado.\n");
            }
        }
    }

    static int parseEntero(String s) {
        // Puede lanzar NumberFormatException o NullPointerException
        return Integer.parseInt(s.trim());
    }
}
