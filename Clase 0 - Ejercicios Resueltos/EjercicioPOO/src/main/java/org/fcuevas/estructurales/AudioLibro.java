package org.fcuevas.estructurales;

import org.fcuevas.interfaces.*;

public class AudioLibro extends Material implements IPrestamo{
    private String narrador;
    private Integer duracionMin;

    public AudioLibro() {
        super();
        this.narrador = null;
        this.duracionMin = null;
    }

    public AudioLibro(Integer codigo, String titulo, String narrador, Integer duracionMin) {
        super(codigo, titulo);
        setNarrador(narrador);
        setDuracionMin(duracionMin);
        diasPermitidos();
    }

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }

    @Override
    public void diasPermitidos() {
        setDiasBasePrestamo(getDiasBasePrestamo() + 10);
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
                AUDIO LIBRO
                ----------------
                - Código: %d
                - Título: %s
                - Días Base Préstamo: %d
                - Disponible: %b
                - Narrador: %s
                - Duración Minutos: %d
                """,getCodigo(),getTitulo(),getDiasBasePrestamo(),getDisponible(),getNarrador(),getDuracionMin());
    }
}
