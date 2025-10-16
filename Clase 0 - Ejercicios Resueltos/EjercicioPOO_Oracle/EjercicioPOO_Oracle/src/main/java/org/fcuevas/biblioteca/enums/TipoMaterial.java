package org.fcuevas.biblioteca.enums;

/** Tipos válidos según la BD: LIBRO | REVISTA | AUDIOLIBRO */
public enum TipoMaterial {
    LIBRO, REVISTA, AUDIOLIBRO;

    /** Valor tal como se guarda en BD (VARCHAR2, mayúsculas). */
    public String aValorBD() { return name(); }

    /** Parse seguro desde BD (ignora espacios y case). */
    public static TipoMaterial desdeBD(String v) {
        if (v == null) return null;
        return TipoMaterial.valueOf(v.trim().toUpperCase());
    }
}