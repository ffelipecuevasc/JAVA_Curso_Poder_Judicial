package org.fcuevas.biblioteca.dto;

import java.time.LocalDate;
import org.fcuevas.biblioteca.enums.*;

public class PrestamoDTO {

    private Integer codigo;
    private Integer codigoMaterial;
    private String rutUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;
    private double multaAplicada;
    private int diasAtraso;

    public PrestamoDTO() {
    }

    public PrestamoDTO(Integer codigo, Integer codigoMaterial, String rutUsuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion, EstadoPrestamo estado, double multaAplicada, int diasAtraso) {
        this.codigo = codigo;
        this.codigoMaterial = codigoMaterial;
        this.rutUsuario = rutUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.multaAplicada = multaAplicada;
        this.diasAtraso = diasAtraso;
    }

    public PrestamoDTO(Integer codigoMaterial, String rutUsuario, LocalDate fechaPrestamo) {
        this(null, codigoMaterial, rutUsuario, fechaPrestamo, null, EstadoPrestamo.ACTIVO, 0.0, 0);
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

    public String getRutUsuario() {
        return rutUsuario;
    }

    public void setRutUsuario(String rutUsuario) {
        this.rutUsuario = rutUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public double getMultaAplicada() {
        return multaAplicada;
    }

    public void setMultaAplicada(double multaAplicada) {
        this.multaAplicada = multaAplicada;
    }

    public int getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(int diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    @Override
    public String toString() {
        return "PrestamoDTO{" +
                "codigo=" + codigo +
                ", codigoMaterial=" + codigoMaterial +
                ", rutUsuario='" + rutUsuario + '\'' +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", estado=" + estado +
                ", multaAplicada=" + multaAplicada +
                ", diasAtraso=" + diasAtraso +
                '}';
    }
}
