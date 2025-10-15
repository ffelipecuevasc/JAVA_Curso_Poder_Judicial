package org.fcuevas.interfaces;

public interface IPrestamo {

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

    Boolean puedePrestarse();

    void marcarPrestado();

    void marcarDevuelto();
}
