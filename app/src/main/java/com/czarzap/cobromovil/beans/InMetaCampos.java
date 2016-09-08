package com.czarzap.cobromovil.beans;

import java.io.Serializable;

/**
 * Created by Alfredo on 29/07/2016.
 */
public class InMetaCampos {

    MetaCamposId id = new MetaCamposId();
    private String mc_valor;
    private String mc_nombre;

    public MetaCamposId getId() { return id; }
    public void setId(MetaCamposId id) { this.id = id; }
    public Integer getMc_empresa() { return id.mc_empresa; }
    public void setMc_empresa(Integer mcEmpresa) { id.mc_empresa = mcEmpresa; }
    public Integer getMc_tabla() { return id.mc_tabla; }
    public void setMc_tabla(Integer mcTabla) { id.mc_tabla = mcTabla; }
    public String getMc_campo() { return id.mc_campo; }
    public void setMc_campo(String mcCampo) { id.mc_campo = mcCampo; }

    public InMetaCampos() { super(); }
    public InMetaCampos(Integer empresa, Integer tabla, String campo) {
        this.id.setMc_empresa(empresa);
        this.id.setMc_tabla(tabla);
        this.id.setMc_campo(campo);
    }
    public String getMc_valor() { return mc_valor; }
    public void setMc_valor(String mcValor) { mc_valor = mcValor; }
    public String getMc_nombre() { return mc_nombre; }
    public void setMc_nombre(String mcNombre) { mc_nombre = mcNombre; }

    @Override
    public String toString() {
        return "InMetaCampos [id=" + id.toString() + ", mc_valor=" + mc_valor + ", mc_nombre=" + mc_nombre + "]";
    }


}

class MetaCamposId implements Serializable {
    private static final long serialVersionUID = 1L;

    Integer mc_empresa;
    String mc_campo;
    Integer mc_tabla;

    public Integer getMc_empresa() { return mc_empresa; }
    public void setMc_empresa(Integer mcEmpresa) { mc_empresa = mcEmpresa; }
    public String getMc_campo() { return mc_campo; }
    public void setMc_campo(String mcCampo) { mc_campo = mcCampo; }
    public Integer getMc_tabla() { return mc_tabla; }
    public void setMc_tabla(Integer mcTabla) { mc_tabla = mcTabla; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mc_campo == null) ? 0 : mc_campo.hashCode());
        result = prime * result
                + ((mc_empresa == null) ? 0 : mc_empresa.hashCode());
        result = prime * result
                + ((mc_tabla == null) ? 0 : mc_tabla.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MetaCamposId))
            return false;
        MetaCamposId other = (MetaCamposId) obj;
        if (mc_campo == null) {
            if (other.mc_campo != null)
                return false;
        } else if (!mc_campo.equals(other.mc_campo))
            return false;
        if (mc_empresa == null) {
            if (other.mc_empresa != null)
                return false;
        } else if (!mc_empresa.equals(other.mc_empresa))
            return false;
        if (mc_tabla == null) {
            if (other.mc_tabla != null)
                return false;
        } else if (!mc_tabla.equals(other.mc_tabla))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MetaCamposId [mc_empresa=" + mc_empresa + ", mc_campo=" + mc_campo + ", mc_tabla=" + mc_tabla + "]";
    }
}
