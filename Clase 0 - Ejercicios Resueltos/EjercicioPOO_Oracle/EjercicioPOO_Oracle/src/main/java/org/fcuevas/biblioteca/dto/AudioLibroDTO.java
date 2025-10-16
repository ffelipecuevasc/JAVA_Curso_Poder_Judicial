package org.fcuevas.biblioteca.dto;

public class AudioLibroDTO {

    private Integer codigo;
    private Integer codigoMaterial;
    private String narrador;
    private Integer duracionMin;
    private Boolean licenciaDigital;

    public AudioLibroDTO() {
    }

    public AudioLibroDTO(Integer codigo, Integer codigoMaterial, String narrador, Integer duracionMin, Boolean licenciaDigital) {
        this.codigo = codigo;
        this.codigoMaterial = codigoMaterial;
        this.narrador = narrador;
        this.duracionMin = duracionMin;
        this.licenciaDigital = licenciaDigital;
    }

    // Constructor de conveniencia para crear (PK null; requiere FK + datos clave).
    public AudioLibroDTO(Integer codigoMaterial, String narrador, Integer duracionMin, Boolean licenciaDigital){
        this(null, codigoMaterial, narrador, duracionMin, licenciaDigital);
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

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }

    public Boolean getLicenciaDigital() {
        return licenciaDigital;
    }

    public void setLicenciaDigital(Boolean licenciaDigital) {
        this.licenciaDigital = licenciaDigital;
    }

    @Override
    public String toString() {
        return "AudioLibroDTO{" +
                "codigo=" + codigo +
                ", codigoMaterial=" + codigoMaterial +
                ", narrador='" + narrador + '\'' +
                ", duracionMin=" + duracionMin +
                ", licenciaDigital=" + licenciaDigital +
                '}';
    }
}
