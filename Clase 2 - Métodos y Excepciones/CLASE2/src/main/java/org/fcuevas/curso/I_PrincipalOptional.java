package org.fcuevas.curso;

import java.util.Optional;

public class I_PrincipalOptional {
    public static void main(String[] args) {
        Optional<String> resultado = buscarUsuario("ana");
        // Manejo declarativo:
        resultado.ifPresentOrElse(
                u -> System.out.println("Encontrado: " + u),
                () -> System.out.println("No encontrado")
        );

        // O decidir qué hacer si no aparece:
        String nombre = resultado.orElse("invitado");
        System.out.println("Usando: " + nombre);
    }

    public static Optional<String> buscarUsuario(String nombre) {
        if (nombre == null) return Optional.empty();
        if (nombre.equalsIgnoreCase("ana")) return Optional.of("Ana");
        return Optional.empty();
    }
}
