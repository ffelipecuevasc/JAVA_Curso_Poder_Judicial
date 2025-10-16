package org.fcuevas.biblioteca.dto;

import java.util.Objects;

public class RevistaDTO {

    private Integer codigo;
    private Integer codigoMaterial;
    private Integer numeroEdicion;
    private String periodicidad;

    public RevistaDTO() {
    }

    public RevistaDTO(Integer codigo, Integer codigoMaterial, Integer numeroEdicion, String periodicidad) {
        this.codigo = codigo;
        this.codigoMaterial = codigoMaterial;
        this.numeroEdicion = numeroEdicion;
        this.periodicidad = periodicidad;
    }

    // Constructor de conveniencia para crear (PK null; requiere FK + datos clave).
    public RevistaDTO(Integer codigoMaterial, Integer numeroEdicion, String periodicidad) {
        this(null, codigoMaterial, numeroEdicion, periodicidad);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigoMaterial() {
        return codigoMaterial;
    }

    public void setCodigoMaterial(Integer codigoMaterial) {
        this.codigoMaterial = codigoMaterial;
    }

    public Integer getNumeroEdicion() {
        return numeroEdicion;
    }

    public void setNumeroEdicion(Integer numeroEdicion) {
        this.numeroEdicion = numeroEdicion;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    @Override
    public String toString() {
        return "RevistaDTO{" +
                "codigo=" + codigo +
                ", codigoMaterial=" + codigoMaterial +
                ", numeroEdicion=" + numeroEdicion +
                ", periodicidad='" + periodicidad + '\'' +
                '}';
    }
}
