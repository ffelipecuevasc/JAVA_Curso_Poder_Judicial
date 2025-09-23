package org.fcuevas.curso;

public class D_PrincipalValidaciones {

    public static void main(String[] args) {
        System.out.println(esRolValido("RIT-123-2025"));  // true
        System.out.println(esRolValido("rit-123-2025"));  // true (ignoramos mayúsculas)
        System.out.println(esRolValido("ABC-12-2025"));   // false

        System.out.println(esFechaValida("20-09-2025"));  // true
        System.out.println(esFechaValida("20/09/2025"));  // false
        System.out.println(esFechaValida("99-99-9999"));  // true *de forma sintáctica*
        // Ojo: esta validación es SINTÁCTICA; validar calendario real requiere parsear.
    }

    // Formato simple: RIT-<3 a 5 dígitos>-<año de 4 dígitos>, case-insensitive
    static boolean esRolValido(String rol) {
        return rol != null && rol.trim().toUpperCase().matches("RIT-\\d{3,5}-\\d{4}");
    }

    // Formato simple: dd-MM-yyyy (solo sintaxis, no calendario)
    static boolean esFechaValida(String fecha) {
        return fecha != null && fecha.trim().matches("\\d{2}-\\d{2}-\\d{4}");
    }
}
