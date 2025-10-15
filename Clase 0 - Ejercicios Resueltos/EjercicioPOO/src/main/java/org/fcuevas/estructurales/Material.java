package org.fcuevas.estructurales;

public abstract class Material {
    private Integer codigo;
    private String titulo;
    private Integer diasBasePrestamo;
    private Boolean disponible;

    public Material(Integer codigo, String titulo) {
        setCodigo(codigo);
        setTitulo(titulo);
        this.diasBasePrestamo = 5;
        this.disponible = true;
    }

    public Material() {
        this.codigo = null;
        this.titulo = null;
        this.diasBasePrestamo = 5;
        this.disponible = true;
    }

    public abstract void diasPermitidos();

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        if(codigo != null && codigo > 0) this.codigo = codigo;
        else throw new IllegalArgumentException("[ERROR] ❌ El código es inválido.");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if(titulo != null && !(titulo.isBlank())) this.titulo = titulo.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ El título es inválido.");
    }

    public Integer getDiasBasePrestamo() {
        return diasBasePrestamo;
    }

    public void setDiasBasePrestamo(Integer diasBasePrestamo) {
        this.diasBasePrestamo = diasBasePrestamo;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}