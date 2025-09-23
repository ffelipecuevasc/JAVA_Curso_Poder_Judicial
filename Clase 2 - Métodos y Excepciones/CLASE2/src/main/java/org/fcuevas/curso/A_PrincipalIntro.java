package org.fcuevas.curso;

public class A_PrincipalIntro {
    public static void main(String[] args) {
        imprimirBanner();
        saludar("Felipe");
        int resultado = sumar(3, 5);
        System.out.println("La suma es: " + resultado);
    }

    static void imprimirBanner() {
        System.out.println("""
                =====================
                  DEMO DE MÉTODOS
                =====================
                """);
    }

    static void saludar(String nombre) {
        System.out.println("Hola, " + nombre + "!");
    }

    static int sumar(int a, int b) {
        return a + b;
    }
}
