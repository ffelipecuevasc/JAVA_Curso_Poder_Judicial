package org.fcuevas.estructurales;

import org.fcuevas.interfaces.*;

public class Libro extends Material implements IPrestamo{
    private String autor, isbn;
    private Integer anio;

    public Libro() {
        super();
        this.autor = null;
        this.anio = null;
        this.isbn = null;
    }

    public Libro(Integer codigo, String titulo, String autor, Integer anio, String isbn) {
        super(codigo, titulo);
        setAutor(autor);
        setAnio(anio);
        setIsbn(isbn);
        diasPermitidos();
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        if(autor != null && !(autor.isBlank())) this.autor = autor.trim();
        else throw new IllegalArgumentException("[ERROR] El autor es inválido.");
    }

    public Integer getAnio() {
        return this.anio;
    }

    public void setAnio(Integer anio) {
        if(anio != null && anio > 0) this.anio = anio;
        else throw new IllegalArgumentException("[ERROR] ❌ El año es inválido.");
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        if(isbn != null && !(isbn.isBlank())) this.isbn = isbn.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ El ISBN es inválido.");
    }

    @Override
    public void diasPermitidos() {
        setDiasBasePrestamo(getDiasBasePrestamo() + 15);
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
                LIBRO
                ----------------
                - Código: %d
                - Título: %s
                - Días Base Préstamo: %d
                - Disponible: %b
                - Autor: %s
                - Año: %d
                - ISBN: %s
                """,getCodigo(),getTitulo(),getDiasBasePrestamo(),getDisponible(),getAutor(),getAnio(),getIsbn());
    }
}
