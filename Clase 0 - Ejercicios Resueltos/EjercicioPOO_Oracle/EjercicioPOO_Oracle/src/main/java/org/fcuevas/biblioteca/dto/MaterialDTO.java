package org.fcuevas.biblioteca.dto;

import org.fcuevas.biblioteca.enums.*;

public class MaterialDTO {
    private Integer codigo;
    private String titulo;
    private TipoMaterial tipo;
    private int diasBase;
    private boolean disponible;

    public MaterialDTO() {
    }

    public MaterialDTO(Integer codigo, String titulo, TipoMaterial tipo, int diasBase, boolean disponible) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.tipo = tipo;
        this.diasBase = diasBase;
        this.disponible = disponible;
    }

    // Constructor de conveniencia para crear (sin código; disponible por defecto en true).
    public MaterialDTO(String titulo, TipoMaterial tipo, int diasBase) {
        this(null, titulo, tipo, diasBase, true);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    public int getDiasBase() {
        return diasBase;
    }

    public void setDiasBase(int diasBase) {
        this.diasBase = diasBase;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "codigo=" + codigo +
                ", titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                ", diasBase=" + diasBase +
                ", disponible=" + disponible +
                '}';
    }
}
