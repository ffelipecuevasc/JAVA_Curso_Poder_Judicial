package org.fcuevas.estructurales;

import org.fcuevas.interfaces.IMulta;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo implements IMulta {

    /*
    * --------------
    * Atributos privado estático de la clase Prestamo
    * que permite generar un código auto incrementable
    * con inicio en 1
    * --------------
    * */

    private static Integer contador = 1;
    private Integer codigo = 1;

    /*
     * --------------
     * Metodo de instancia que permite incrementar el
     * código antes de cada nueva instancia de Prestamo
     * --------------
     * */

    {
        contador++;
        this.codigo = this.codigo + contador;
    }

    /*
     * --------------
     * Atributos privados propios de la clase Prestamo
     * --------------
     * */

    private DateTimeFormatter formatoFecha;
    private Integer codigoMaterial, diasAtraso, multaAplicada;
    private String runUsuario, fechaPrestamoString, fechaDevolucionString;
    private LocalDate fechaPrestamo, fechaDevolucion;

    /*
    * --------------
    * Constructores de la clase Prestamo (sobrecarga de constructores):
    * - Constructor sin parámetros que inicializa los atributos en null
    * - Constructor con parámetros que inicializa los atributos con ellos
    * --------------
    * */

    public Prestamo() {
        this.codigoMaterial = null;
        this.diasAtraso = null;
        this.multaAplicada = null;
        this.runUsuario = null;
        this.fechaPrestamoString = null;
        this.fechaDevolucionString = null;
    }

    public Prestamo(Integer codigoMaterial, String runUsuario, String fechaPrestamoString, String fechaDevolucionString, Material material) {
        setCodigoMaterial(codigoMaterial);
        setRunUsuario(runUsuario);
        setFormatoFecha();
        setFechaPrestamoString(fechaPrestamoString);
        setFechaDevolucionString(fechaDevolucionString);
        this.diasAtraso = calcularDiasAtraso(this.fechaPrestamo, this.fechaDevolucion, material);
        this.multaAplicada = calcularMulta(this.diasAtraso);
    }

    /*
     * --------------
     * Getters y Setters de la clase Prestamo:
     * - Getters comunes y corrientes
     * - Setters con pequeñas validaciones y generación de una excepción en casos inválidos
     * --------------
     * */

    public Integer getCodigo() {
        return codigo;
    }

    public boolean setCodigo(Integer codigo) {
        if (codigo != null && codigo >= 0) {
            this.codigo = codigo;
            return true;
        } else {
            return false;
        }
    }

    public Integer getCodigoMaterial() {
        return codigoMaterial;
    }

    public void setCodigoMaterial(Integer codigoMaterial) {
        if (codigoMaterial != null && codigoMaterial >= 0) this.codigoMaterial = codigoMaterial;
        else throw new IllegalArgumentException("[ERROR] ❌ Código material inválido.");
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(Integer diasAtraso) {
        if (diasAtraso != null && diasAtraso >= 0) this.diasAtraso = diasAtraso;
        else throw new IllegalArgumentException("[ERROR] ❌ Días de atraso inválidos.");
    }

    public Integer getMultaAplicada() {
        return multaAplicada;
    }

    public void setMultaAplicada(Integer multaAplicada) {
        if (multaAplicada != null && multaAplicada >= 0) this.multaAplicada = multaAplicada;
        else throw new IllegalArgumentException("[ERROR] ❌ Multa aplicada inválida.");
    }

    public String getRunUsuario() {
        return runUsuario;
    }

    public void setRunUsuario(String runUsuario) {
        if (runUsuario != null && !(runUsuario.isBlank())) this.runUsuario = runUsuario.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ Run de usuario inválido.");
    }

    public DateTimeFormatter getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha() {
        this.formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public String getFechaPrestamoString() {
        return fechaPrestamoString;
    }

    public void setFechaPrestamoString(String fechaPrestamoString) {
        if (fechaPrestamoString != null && !(fechaPrestamoString.isBlank())) {
            this.fechaPrestamoString = fechaPrestamoString.trim();
            setFechaPrestamo(this.fechaPrestamoString);
        }
        else throw new IllegalArgumentException("[ERROR] ❌ Fecha de préstamo inválida.");
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = LocalDate.parse(fechaPrestamo, this.formatoFecha);
    }

    public String getFechaDevolucionString() {
        return fechaDevolucionString;
    }

    public void setFechaDevolucionString(String fechaDevolucionString) {
        if (fechaDevolucionString != null && !(fechaDevolucionString.isBlank())) {
            this.fechaDevolucionString = fechaDevolucionString.trim();
            setFechaDevolucion(this.fechaDevolucionString);
        }
        else throw new IllegalArgumentException("[ERROR] ❌ Fecha de devolución inválida.");
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = LocalDate.parse(fechaDevolucion, this.formatoFecha);
    }

    @Override
    public Integer calcularDiasAtraso(LocalDate fecha1, LocalDate fecha2, Material material) {
        Integer dias = (int) ChronoUnit.DAYS.between(fecha1, fecha2);
        if(material.getDiasBasePrestamo() >= dias) return 0;
        else return dias - material.getDiasBasePrestamo();
    }

    @Override
    public Integer calcularMulta(int diasAtraso) {
        return diasAtraso * 5000;
    }

    /*
     * --------------
     * Otros métodos:
     * - toString (sobrescritura del original proveniente de Object)
     * --------------
     * */

    @Override
    public String toString() {
        return String.format("""
                ----------------
                PRÉSTAMO
                ----------------
                - Código: %d
                - Código Material: %d
                - RUN Usuario: %s
                - Fecha Préstamo: %s
                - Fecha Devolución Real: %s
                - Días de Atraso: %d
                - Multa Aplicada: %d
                """, getCodigo(), getCodigoMaterial(), getRunUsuario(), getFechaPrestamoString(), getFechaDevolucionString(), getDiasAtraso(), getMultaAplicada());
    }
}
