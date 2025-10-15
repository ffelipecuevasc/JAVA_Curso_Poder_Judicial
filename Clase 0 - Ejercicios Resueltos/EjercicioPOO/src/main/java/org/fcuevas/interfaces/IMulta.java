package org.fcuevas.interfaces;

import java.time.LocalDate;
import org.fcuevas.estructurales.Material;

public interface IMulta {

    /*
     * En Java, los métodos de una interfaz son implícitamente:
     * - public
     * - abstract
     *
     * No es necesario especificar estos detalles a menos que los métodos sean:
     * - default
     * - static
     *
     * La idea es que las clases hijo de Material implemente estas interfaces y
     * sus respectivos métodos según corresponda.
     * */

    Integer calcularDiasAtraso(LocalDate fecha1, LocalDate fecha2, Material material);
    Integer calcularMulta(int diasAtraso);
}
