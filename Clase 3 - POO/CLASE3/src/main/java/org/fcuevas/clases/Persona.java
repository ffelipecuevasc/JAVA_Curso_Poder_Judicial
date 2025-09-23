package org.fcuevas.clases;

public class Persona {

    private String run, nombres, apellidos, email, telefono, domicilio;

    public Persona() {
        this.run = "";
        this.nombres = "";
        this.apellidos = "";
        this.email = "";
        this.telefono = "";
        this.domicilio = "";
    }

    public Persona(String run, String nombres, String apellidos) {
        this.run = normalizarRun(run);
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = "";
        this.telefono = "";
        this.domicilio = "";
    }

    // GETTERS

    public String getRun() {
        return run;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    // SETTERS

    public final void setRun(String r) {
        String normalizado = normalizarRun(r);
        this.run = normalizado;
    }

    public final void setNombres(String nombres) {
        if (nombres == null || nombres.trim().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombres = nombres.trim();
    }

    public final void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }
        this.apellidos = apellidos.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            this.email = null;
            return;
        }
        this.email = email.trim();
    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            this.telefono = null;
            return;
        }
        this.telefono = telefono.trim();
    }

    public void setDomicilio(String domicilio) {
        if (domicilio == null || domicilio.isBlank()) {
            throw new IllegalArgumentException("El domicilio es obligatorio.");
        }
        this.domicilio = domicilio.trim();
    }

    // MÉTODOS UTILITARIOS

    public String nombreCompleto() {
        return (nombres + " " + apellidos).trim();
    }

    public static String normalizarRun(String r) {
        if (r == null) throw new IllegalArgumentException("RUN obligatorio.");
        String rAux = r.trim().toUpperCase();
        return rAux;
    }

    public String contactoPreferente() {
        if (email != null && !email.isBlank()) return email;
        if (telefono != null && !telefono.isBlank()) return telefono;
        return "Sin contacto.";
    }

    @Override
    public String toString() {
        return "Persona{" +
                "run='" + run + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", domicilio='" + domicilio + '\'' +
                '}';
    }
}