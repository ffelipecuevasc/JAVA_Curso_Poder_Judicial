package org.fcuevas.curso;

import java.time.LocalDate;

public class Caso {
    private Long id;
    private String rol;
    private String caratulado;
    private LocalDate fechaIngreso;
    private EstadoCaso estado;

    public Caso() {
        this.id = null;
        this.rol = null;
        this.caratulado = null;
        this.fechaIngreso = null;
        this.estado = null;
    }

    public Caso(String rol, String caratulado, LocalDate fechaIngreso, EstadoCaso estado) {
        this.id = null;
        this.rol = rol;
        this.caratulado = caratulado;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Caso(Long id, String rol, String caratulado, LocalDate fechaIngreso, EstadoCaso estado) {
        this.id = id;
        this.rol = rol;
        this.caratulado = caratulado;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCaratulado() {
        return caratulado;
    }

    public void setCaratulado(String caratulado) {
        this.caratulado = caratulado;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public EstadoCaso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaso estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Caso{" +
                "id=" + id +
                ", rol='" + rol + '\'' +
                ", caratulado='" + caratulado + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", estado=" + estado +
                '}';
    }
}
