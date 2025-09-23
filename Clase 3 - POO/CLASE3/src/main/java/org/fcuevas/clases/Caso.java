package org.fcuevas.clases;

import org.fcuevas.enums.*;
import java.time.LocalDate;

public class Caso {

    private String rol, caratulado;
    private LocalDate fechaIngreso;
    private EstadoCaso estado;

    public Caso(String rol, String caratulado, LocalDate fechaIngreso) {
        this(rol, caratulado, fechaIngreso, EstadoCaso.ABIERTO);
    }

    public Caso(String rol, String caratulado, LocalDate fechaIngreso, EstadoCaso estadoInicial) {
        setRol(rol);
        setCaratulado(caratulado);
        setFechaIngreso(fechaIngreso);
        setEstado(estadoInicial);
    }

    public String getRol() {
        return rol;
    }

    public String getCaratulado() {
        return caratulado;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public EstadoCaso getEstado() {
        return estado;
    }

    public final void setRol(String rol) {
        String r = normalizarRol(rol);
        this.rol = r;
    }

    public final void setCaratulado(String caratulado) {
        if (caratulado == null || caratulado.trim().isBlank()) {
            throw new IllegalArgumentException("El caratulado es obligatorio.");
        }
        this.caratulado = caratulado.trim();
    }

    public final void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    public final void setEstado(EstadoCaso estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        this.estado = estado;
    }

    public static String normalizarRol(String rol) {
        if (rol == null) throw new IllegalArgumentException("ROL obligatorio.");
        return rol.trim().toUpperCase();
    }

    public void cambiarEstado(EstadoCaso nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }
        this.estado = nuevoEstado;
    }

    public String imprimirResumen() {
        return """
                ====== CASO %s ======
                Caratulado       : %s
                Fecha de ingreso : %s
                Estado           : %s
                """.formatted(rol, caratulado, fechaIngreso, estado);
    }

    @Override
    public String toString() {
        return "Caso{" +
                "rol='" + rol + '\'' +
                ", caratulado='" + caratulado + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", estado=" + estado +
                '}';
    }
}