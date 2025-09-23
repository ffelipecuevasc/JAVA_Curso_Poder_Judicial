package org.fcuevas.clases;

import org.fcuevas.enums.*;

public class Representante extends Persona {
    private TipoRepresentante tipo;
    private boolean mandatoVigente;
    private String institucion;

    public Representante(String run, String nombres, String apellidos, TipoRepresentante tipo) {
        super(run, nombres, apellidos);
        setTipo(tipo);
        this.mandatoVigente = false; // por defecto, sin mandato hasta activarlo
    }

    public TipoRepresentante getTipo() {
        return tipo;
    }

    public final void setTipo(TipoRepresentante tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de representante es obligatorio.");
        this.tipo = tipo;
    }

    public void setMandatoVigente() {
        this.mandatoVigente = true;
    }

    public boolean getMandatoVigencia() {
        return mandatoVigente;
    }

    public void revocarMandatoVigente() {
        this.mandatoVigente = false;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        if(institucion == null || institucion.isBlank()){
            institucion = null;
        }else{
            this.institucion = institucion;
        }
    }

    public boolean esAbogado() {
        if(this.tipo == TipoRepresentante.ABOGADO || this.tipo == TipoRepresentante.DEFENSOR_PUBLICO){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "Representante{" +
                "tipo=" + tipo +
                ", mandatoVigente=" + mandatoVigente +
                ", institucion='" + institucion + '\'' +
                '}';
    }
}