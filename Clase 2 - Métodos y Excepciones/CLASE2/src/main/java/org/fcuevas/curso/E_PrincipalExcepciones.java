package org.fcuevas.curso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class E_PrincipalExcepciones {

    public static void main(String[] args) {
        // Unchecked: error lógico del programador/entrada inválida
        try {
            int r = dividir(10, 0);
            System.out.println(r);
        } catch (IllegalArgumentException e) {
            System.out.println("[UNCHECKED] " + e.getMessage());
        }

        // Checked: I/O puede fallar por el entorno → hay que manejarlo
        try {
            leerArchivo("no-existe.txt"); // lanza IOException (checked)
        } catch (IOException e) {
            System.out.println("[CHECKED] " + e.getMessage());
        }
    }

    public static int dividir(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("División por cero");
        return a / b;
    }

    public static void leerArchivo(String nombre) throws IOException {
        Files.readAllLines(Path.of(nombre));
    }
}
