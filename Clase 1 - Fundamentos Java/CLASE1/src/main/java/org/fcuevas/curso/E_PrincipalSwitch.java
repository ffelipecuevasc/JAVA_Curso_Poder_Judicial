package org.fcuevas.curso;

public class E_PrincipalSwitch {
    public static void main(String[] args) {
        switchExpression();
        switchClasico();
    }

    private static void switchClasico() {
        String dia = "SAB"; // SAB, DOM, LUN...

        // switch CLÁSICO: NO devuelve valor directamente
        int horarioApertura;

        switch (dia) {
            case "SAB":
            case "DOM":
                horarioApertura = 10; // Fines de semana abre a las 10
                break; // ¡OBLIGATORIO! Sin break continuaría al siguiente caso

            case "LUN":
            case "MAR":
            case "MIE":
            case "JUE":
            case "VIE":
                horarioApertura = 9; // Entre semana abre a las 9
                break; // ¡OBLIGATORIO!

            default:
                // Bloque con lógica y NO usa 'yield'
                System.out.println("Día desconocido, uso horario por defecto.");
                horarioApertura = 9; // Asignación normal, NO yield
                break; // Técnicamente opcional en default, pero buena práctica
        }

        System.out.println("Abre a las: " + horarioApertura + ":00");
    }

    private static void switchExpression() {
        String dia = "SAB"; // SAB, DOM, LUN...

        // switch como EXPRESIÓN: devuelve un valor
        int horarioApertura = switch (dia) {
            case "SAB", "DOM" -> 10;        // fines de semana abre a las 10
            case "LUN", "MAR", "MIE", "JUE", "VIE" -> 9;
            default -> {
                // Bloque con lógica y 'yield' si necesitas varias líneas
                System.out.println("Día desconocido, uso horario por defecto.");
                yield 9;
            }
        };

        System.out.println("Abre a las: " + horarioApertura + ":00");

        // También puede usarse como 'switch statement' clásico si NO necesitas un valor de retorno.
    }
}