package org.fcuevas.clases;

import org.fcuevas.enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parte extends Persona {
    private RolEnCausa rolEnCausa;
    private List<Representante> representantes;

    public Parte(String run, String nombres, String apellidos, RolEnCausa rolEnCausa) {
        super(run, nombres, apellidos);
        setRolEnCausa(rolEnCausa);
        representantes = new ArrayList<>();
    }

    public RolEnCausa getRolEnCausa() {
        return rolEnCausa;
    }

    public final void setRolEnCausa(RolEnCausa rolEnCausa) {
        if (rolEnCausa == null) {
            throw new IllegalArgumentException("El rol en la causa es obligatorio.");
        }
        this.rolEnCausa = rolEnCausa;
    }

    public List<Representante> getRepresentantes() {
        return this.representantes;
    }

    public int getCantidadRepresentantes() {
        return this.representantes.size();
    }

    public void agregarRepresentante(Representante r) {
        if (r == null) throw new IllegalArgumentException("Representante no puede ser nulo.");
        String runRep = Persona.normalizarRun(r.getRun());

        // Duplicados por RUT
        if (buscarRepresentantePorRun(runRep).isPresent()) {
            throw new IllegalArgumentException("Ya existe un representante con RUN " + runRep);
        }

        representantes.add(r);
    }

    public boolean removerRepresentantePorRun(String run) {
        String runNorm = Persona.normalizarRun(run);
        Optional<Representante> rep = buscarRepresentantePorRun(runNorm);
        if(rep.isPresent()) {
            return representantes.remove(rep.get());
        }
        return false;
    }

    public Optional<Representante> buscarRepresentantePorRun(String run) {
        String runNorm = Persona.normalizarRun(run);
        for(Representante r : representantes){
            if(r.getRun().equals(runNorm)) return Optional.of(r);
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Parte{" +
                "rolEnCausa=" + rolEnCausa +
                ", representantes=" + representantes +
                '}';
    }
}