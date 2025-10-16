package org.fcuevas.biblioteca.dto;

public class LibroDTO {
    private Integer codigo;
    private Integer codigoMaterial;
    private String autor;
    private Integer año;
    private String isbn;

    public LibroDTO() {

    }

    public LibroDTO(Integer codigo, Integer codigoMaterial, String autor, Integer año, String isbn) {
        this.codigo = codigo;
        this.codigoMaterial = codigoMaterial;
        this.autor = autor;
        this.año = año;
        this.isbn = isbn;
    }

    // Constructor de conveniencia para crear (PK null; requiere FK + datos clave).
    public LibroDTO(Integer codigoMaterial, String autor, Integer año, String isbn) {
        this(null, codigoMaterial, autor, año, isbn);
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "LibroDTO{" +
                "codigo=" + codigo +
                ", codigoMaterial=" + codigoMaterial +
                ", autor='" + autor + '\'' +
                ", año=" + año +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
