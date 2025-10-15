package org.fcuevas.estructurales;

public class Usuario {
    private String run, nombre, apellido, email;
    private Integer cupoPrestamosActual, cupoPrestamosMaximo;

    public Usuario() {
        this.run = null;
        this.nombre = null;
        this.apellido = null;
        this.email = null;
        this.cupoPrestamosActual = null;
        this.cupoPrestamosMaximo = null;
    }

    public Usuario(String run, String nombre, String apellido, String email, Integer cupoPrestamosActual, Integer cupoPrestamosMaximo) {
        setRun(run);
        setNombre(nombre);
        setApellido(apellido);
        setEmail(email);
        setCupoPrestamosActual(cupoPrestamosActual);
        setCupoPrestamosMaximo(cupoPrestamosMaximo);
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        if(run != null && !(run.isBlank())) this.run = run.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ Run inválido.");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre != null && !(nombre.isBlank())) this.nombre = nombre.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ Nombre inválido.");
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if(apellido != null && !(apellido.isBlank())) this.apellido = apellido.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ Apellido inválido.");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email != null && !(email.isBlank())) this.email = email.trim();
        else throw new IllegalArgumentException("[ERROR] ❌ Email inválido.");
    }

    public Integer getCupoPrestamosActual() {
        return cupoPrestamosActual;
    }

    public void setCupoPrestamosActual(Integer cupoPrestamosActual) {
        if(cupoPrestamosActual != null && cupoPrestamosActual >= 0) this.cupoPrestamosActual = cupoPrestamosActual;
        else throw new IllegalArgumentException("[ERROR] ❌ Cupo de préstamo actual inválido.");
    }

    public Integer getCupoPrestamosMaximo() {
        return cupoPrestamosMaximo;
    }

    public void setCupoPrestamosMaximo(Integer cupoPrestamosMaximo) {
        if(cupoPrestamosMaximo != null && cupoPrestamosMaximo >= 0) this.cupoPrestamosMaximo = cupoPrestamosMaximo;
        else throw new IllegalArgumentException("[ERROR] ❌ Cupo de préstamo máximo inválido.");
    }

    @Override
    public String toString() {
        return String.format("""
                ----------------
                USUARIO
                ----------------
                - RUN: %s
                - Nombre: %s
                - Apellido: %s
                - Email: %s
                - Cupos Préstamo Actual: %d
                - Cupos Préstamo Máximo: %d
                """,getRun(),getNombre(),getApellido(),getEmail(),getCupoPrestamosActual(),getCupoPrestamosMaximo());
    }
}
