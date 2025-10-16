package org.fcuevas.biblioteca.enums;

/** Estados válidos según la BD: ACTIVO | DEVUELTO */
public enum EstadoPrestamo {
    ACTIVO, DEVUELTO;

    public String aValorBD() { return name(); }

    public static EstadoPrestamo desdeBD(String v) {
        if (v == null) return null;
        return EstadoPrestamo.valueOf(v.trim().toUpperCase());
    }
}