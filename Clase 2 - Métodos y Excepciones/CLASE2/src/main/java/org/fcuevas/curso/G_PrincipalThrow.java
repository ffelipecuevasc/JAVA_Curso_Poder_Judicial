package org.fcuevas.curso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class G_PrincipalThrow {

    public static void main(String[] args) {
        try {
            // este método propaga IOException
            String contenido = leerConfig("app.conf");
            System.out.println(contenido);
        } catch (IOException e) {
            System.out.println("[APP] No se pudo leer la config: " + e.getMessage());
        }
    }

    // No sabemos qué hacer si falla leer; propagamos
    static String leerConfig(String archivo) throws IOException {
        if (archivo == null || archivo.isBlank()) {
            throw new IllegalArgumentException("Nombre de archivo inválido"); // unchecked
        }
        return Files.readString(Path.of(archivo));
    }
}
