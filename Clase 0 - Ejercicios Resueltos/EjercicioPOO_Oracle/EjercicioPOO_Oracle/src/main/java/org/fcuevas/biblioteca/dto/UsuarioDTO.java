package org.fcuevas.biblioteca.dto;

import java.time.LocalDate;

public class UsuarioDTO {
    private String rut;
    private String nombre;
    private String email;
    private int cupoMax;
    private int cupoActual;
    private LocalDate fechaCreacion; // corresponde al DATE de Oracle (SYSDATE por defecto)

    public UsuarioDTO() {

    }

    public UsuarioDTO(String rut, String nombre, String email, int cupoMax, int cupoActual, LocalDate fechaCreacion) {
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.cupoMax = cupoMax;
        this.cupoActual = cupoActual;
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor de conveniencia para "crear" (la BD setea fechaCreacion por defecto)
    public UsuarioDTO(String rut, String nombre, String email, int cupoMax) {
        this(rut, nombre, email, cupoMax, 0, null);
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCupoMax() {
        return cupoMax;
    }

    public void setCupoMax(int cupoMax) {
        this.cupoMax = cupoMax;
    }

    public int getCupoActual() {
        return cupoActual;
    }

    public void setCupoActual(int cupoActual) {
        this.cupoActual = cupoActual;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", cupoMax=" + cupoMax +
                ", cupoActual=" + cupoActual +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
