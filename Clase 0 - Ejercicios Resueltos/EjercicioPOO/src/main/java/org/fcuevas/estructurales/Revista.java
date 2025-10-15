package org.fcuevas.estructurales;

import org.fcuevas.interfaces.*;

public class Revista extends Material implements IPrestamo{
    private Integer numeroEdicion, periodicidad;

    public Revista() {
        super();
        this.numeroEdicion = null;
        this.periodicidad = null;
    }

    public Revista(Integer codigo, String titulo, Integer numeroEdicion, Integer periodicidad) {
        super(codigo, titulo);
        setNumeroEdicion(numeroEdicion);
        setPeriodicidad(periodicidad);
        diasPermitidos();
    }

    public Integer getNumeroEdicion() {
        return this.numeroEdicion;
    }

    public void setNumeroEdicion(Integer numeroEdicion) {
        if(numeroEdicion != null && numeroEdicion > 0) this.numeroEdicion = numeroEdicion;
        else throw new IllegalArgumentException("[ERROR] ❌ El número de edición debe ser mayor a 0.");
    }

    public Integer getPeriodicidad() {
        return this.periodicidad;
    }

    public void setPeriodicidad(Integer periodicidad) {
        if(periodicidad != null && periodicidad > 0) this.periodicidad = periodicidad;
        else throw new IllegalArgumentException("[ERROR] ❌ La periodicidad debe ser mayor a 0.");
    }

    @Override
    public void diasPermitidos() {
        setDiasBasePrestamo(getDiasBasePrestamo() + 5);
    }

    @Override
    public Boolean puedePrestarse() {
        if(getDisponible()) return true;
        else return false;
    }

    @Override
    public void marcarPrestado() {
        setDisponible(false);
    }

    @Override
    public void marcarDevuelto() {
        setDisponible(true);
    }

    @Override
    public String toString() {
        return String.format("""
                ----------------
                REVISTA
                ----------------
                - Código: %d
                - Título: %s
                - Días Base Préstamo: %d
                - Disponible: %b
                - Número Edicion: %d
                - Periodicidad: %d
                """,getCodigo(),getTitulo(),getDiasBasePrestamo(),getDisponible(),getNumeroEdicion(),getPeriodicidad());
    }
}
