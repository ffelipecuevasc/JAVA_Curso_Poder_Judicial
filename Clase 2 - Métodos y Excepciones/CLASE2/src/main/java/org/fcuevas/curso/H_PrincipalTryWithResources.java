package org.fcuevas.curso;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class H_PrincipalTryWithResources {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("notas.txt"))) {
            System.out.println("Primera línea: " + br.readLine());
        } catch (IOException e) {
            System.out.println("[I/O] " + e.getMessage());
        }
    }
}
