package org.fcuevas.curso;

public class B_PrincipalVariaciones {

    public static void main(String[] args) {
        // Devuelve 5 (int)
        System.out.println(sumar(2, 3));
        // Devuelve 5.6 (double)
        System.out.println(sumar(2.5, 3.1));
        // Devuelve 6 (varargs)
        System.out.println(sumarTodos(1, 2, 3));
        // Devuelve 0 (funciona con 0 argumentos)
        System.out.println(sumarTodos());
    }

    // Métodos con parámetros especificados
    public static int sumar(int a, int b) {     // a y b son parámetros
        return a + b;                           // retorno
    }

    // Métodos con sobrecarga, mismo nombre pero distinta firma
    public static double sumar(double a, double b) {
        return a + b;
    }

    // Métodos con parámetros 'varargs', recibe varios parámetros en forma de arreglo
    public static int sumarTodos(int... numeros) {
        int total = 0;
        for (int n : numeros) total += n;
        return total;
    }

}
