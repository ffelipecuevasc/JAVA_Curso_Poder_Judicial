package org.fcuevas.curso;

public class F_PrincipalBloques {
    public static void main(String[] args) {
        String ayuda = """
                ================= Ayuda =================
                1) Ingresar datos
                2) Mostrar resultados
                0) Salir
                (Escribe el número y presiona Enter)
                =========================================
                """;
        System.out.println(ayuda);
        // Puedes combinarlos con format si quieres valores dinámicos:
        String ficha = """
                ---- FICHA ----
                Nombre: %s
                Edad:   %d
                ----------------
                """.formatted("Diego", 28);
        System.out.println(ficha);
    }
}