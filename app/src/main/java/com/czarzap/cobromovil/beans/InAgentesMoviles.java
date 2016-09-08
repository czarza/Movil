package com.czarzap.cobromovil.beans;

import java.io.Serializable;
import java.util.Date;


public class InAgentesMoviles {
    InAgentesMovilesPK id = new InAgentesMovilesPK();
    String am_cel;
    String am_nombre;
    String am_rol;
    String am_msg;
    Date am_ult_login;
    String am_status;
    String am_alta;
    String am_baja;
    String am_alta_usr;
    String am_baja_usr;
    String am_horario_ini_militar;
    String am_horario_fin_militar;
    String am_observaciones;
    String am_password;
    public InAgentesMoviles() {}

    public InAgentesMoviles(Integer am_empresa, Integer am_numero) {
        super();
        this.id.am_empresa = am_empresa;
        this.id.am_numero = am_numero;
    }

    public InAgentesMovilesPK getId() { return id; }
    public void setId(InAgentesMovilesPK id) { this.id = id; }
    public Integer getAm_empresa() { return id.am_empresa; }
    public void setAm_empresa(Integer am_empresa) { this.id.am_empresa = am_empresa; }
    public Integer getAm_numero() { return id.am_numero; }
    public void setAm_numero(Integer am_numero) { this.id.am_numero = am_numero; }
    public String getAm_cel() { return am_cel; }
    public void setAm_cel(String am_cel) { this.am_cel = am_cel; }
    public String getAm_nombre() { return am_nombre; }
    public void setAm_nombre(String am_nombre) { this.am_nombre = am_nombre; }
    public String getAm_rol() { return am_rol; }
    public void setAm_rol(String am_rol) { this.am_rol = am_rol; }
    public String getAm_msg() { return am_msg; }
    public void setAm_msg(String am_msg) { this.am_msg = am_msg; }
    public Date getAm_ult_login() { return am_ult_login; }
    public void setAm_ult_login(Date am_ult_login) { this.am_ult_login = am_ult_login; }
    public String getAm_status() { return am_status; }
    public void setAm_status(String am_status) { this.am_status = am_status; }
    public String getAm_alta() { return am_alta; }
    public void setAm_alta(String am_alta) { this.am_alta = am_alta; }
    public String getAm_baja() { return am_baja; }
    public void setAm_baja(String am_baja) { this.am_baja = am_baja; }
    public String getAm_alta_usr() { return am_alta_usr; }
    public void setAm_alta_usr(String am_alta_usr) { this.am_alta_usr = am_alta_usr; }
    public String getAm_baja_usr() { return am_baja_usr; }
    public void setAm_baja_usr(String am_baja_usr) { this.am_baja_usr = am_baja_usr; }
    public String getAm_horario_ini_militar() { return am_horario_ini_militar; }
    public void setAm_horario_ini_militar(String am_horario_ini_militar) { this.am_horario_ini_militar = am_horario_ini_militar; }
    public String getAm_horario_fin_militar() { return am_horario_fin_militar; }
    public void setAm_horario_fin_militar(String am_horario_fin_militar) { this.am_horario_fin_militar = am_horario_fin_militar; }
    public String getAm_observaciones() { return am_observaciones; }
    public void setAm_observaciones(String am_observaciones) { this.am_observaciones = am_observaciones; }
    public String getAm_password() { return am_password; }
    public void setAm_password(String am_password) { this.am_password = am_password; }

    @Override
    public String toString() {
        return "InAgentesMoviles [id=" + id.toString() + ", am_cel=" + am_cel + ", am_nombre=" + am_nombre + ", am_rol=" + am_rol
                + ", am_msg=" + am_msg + ", am_ult_login=" + am_ult_login + ", am_status=" + am_status + ", am_alta="
                + am_alta + ", am_baja=" + am_baja + ", am_alta_usr=" + am_alta_usr + ", am_baja_usr=" + am_baja_usr
                + ", am_horario_ini_militar=" + am_horario_ini_militar + ", am_horario_fin_militar="
                + am_horario_fin_militar + ", am_observaciones=" + am_observaciones + ", am_password=" + am_password
                + "]";
    }

}


class InAgentesMovilesPK implements Serializable {
    private static final long serialVersionUID = 1L;

    Integer am_empresa;
    Integer am_numero;

    public Integer getAm_empresa() { return am_empresa; }
    public void setAm_empresa(Integer am_empresa) { this.am_empresa = am_empresa; }
    public Integer getAm_numero() { return am_numero; }
    public void setAm_numero(Integer am_numero) { this.am_numero = am_numero; }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((am_empresa == null) ? 0 : am_empresa.hashCode());
        result = prime * result + ((am_numero == null) ? 0 : am_numero.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InAgentesMovilesPK other = (InAgentesMovilesPK) obj;
        if (am_empresa == null) {
            if (other.am_empresa != null)
                return false;
        } else if (!am_empresa.equals(other.am_empresa))
            return false;
        if (am_numero == null) {
            if (other.am_numero != null)
                return false;
        } else if (!am_numero.equals(other.am_numero))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "InAgentesMovilesPK [am_empresa=" + am_empresa + ", am_numero=" + am_numero + "]";
    }

}
