package com.czarzap.cobromovil.beans;


public class InEmpresas {

    public InEmpresas() {super();}

    public InEmpresas(Integer empresa) {this.setEm_id(empresa);}

    Integer em_id;
    private String em_nombre;
    private String em_calle;
    private String em_exterior;
    private String em_interior;
    private String em_colonia;
    private String em_municipio;
    private String em_estado;
    private String em_pais;
    private String em_rfc;

    public Integer getEm_id() {return em_id;}
    public void setEm_id(Integer em_id) {this.em_id = em_id;}
    public String getEm_nombre() {return em_nombre;}
    public void setEm_nombre(String em_nombre) {this.em_nombre = em_nombre;}
    public String getEm_calle() {return em_calle;}
    public void setEm_calle(String em_calle) {this.em_calle = em_calle;}
    public String getEm_exterior() {return em_exterior;}
    public void setEm_exterior(String em_exterior) {this.em_exterior = em_exterior;}
    public String getEm_interior() {return em_interior;}
    public void setEm_interior(String em_interior) {this.em_interior = em_interior;}
    public String getEm_colonia() {return em_colonia;}
    public void setEm_colonia(String em_colonia) {this.em_colonia = em_colonia;}
    public String getEm_municipio() {return em_municipio;}
    public void setEm_municipio(String em_municipio) {this.em_municipio = em_municipio;}
    public String getEm_estado() {return em_estado;}
    public void setEm_estado(String em_estado) {this.em_estado = em_estado;}
    public String getEm_pais() {return em_pais;}
    public void setEm_pais(String em_pais) {this.em_pais = em_pais;}
    public String getEm_rfc() {return em_rfc;}
    public void setEm_rfc(String em_rfc) {this.em_rfc = em_rfc;}


    @Override
    public String toString() {
        return "InEmpresas{" +
                "em_id=" + em_id +
                ", em_nombre='" + em_nombre + '\'' +
                ", em_calle='" + em_calle + '\'' +
                ", em_exterior='" + em_exterior + '\'' +
                ", em_interior='" + em_interior + '\'' +
                ", em_colonia='" + em_colonia + '\'' +
                ", em_municipio='" + em_municipio + '\'' +
                ", em_estado='" + em_estado + '\'' +
                ", em_pais='" + em_pais + '\'' +
                ", em_rfc='" + em_rfc + '\'' +
                '}';
    }
}
