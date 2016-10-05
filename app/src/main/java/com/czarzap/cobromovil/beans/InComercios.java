package com.czarzap.cobromovil.beans;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class InComercios implements Serializable {
	public ComerciosId id = new ComerciosId();
	private Integer com_orden;
	private Integer com_contribuyente;
	private String  com_denominacion;
	private String  com_nombre_propietario;
	private String  com_ocupante;
	private BigDecimal  com_frente;
	private BigDecimal  com_fondo;
	private String  com_local;
	private String  com_ruta;
	private String  com_domicilio;
	private String  com_colonia;
	private String  com_domicilio_notificaciones;
	private Integer com_ult_eje;
	private String  com_horario;
	private String  com_status;
	private String  com_usuario;
	private String giros;
	private String  com_niv;
	private String  com_num_placa;
	private String  com_num_tarj_circulacion;
	private String ultFechaPago;
	private String nombreRuta;
	private BigDecimal tarifa;
	private Integer agente;

	public InComercios() {
		super();
	}

	public InComercios(Integer empresa, String tipo, Integer control) {
		this.setCom_empresa(empresa);
		this.setCom_tipo(tipo);
		this.setCom_control(control);
	}
	public Integer getCom_empresa() { return id.com_empresa; }
	public void setCom_empresa(Integer com_empresa) { this.id.com_empresa = com_empresa; }
	public Integer getCom_control() { return id.com_control; }
	public void setCom_control(Integer com_control) { this.id.com_control = com_control; }
	public String getCom_tipo() { return id.com_tipo; }
	public void setCom_tipo(String com_tipo) { this.id.com_tipo = com_tipo; }
	public ComerciosId getId() {return id;}
	public void setId(ComerciosId id) {this.id = id;}
	public Integer getCom_orden() {return com_orden;}
	public void setCom_orden(Integer com_orden) {this.com_orden = com_orden;}
	public Integer getCom_contribuyente() {return com_contribuyente;}
	public void setCom_contribuyente(Integer com_contribuyente) {this.com_contribuyente = com_contribuyente;}
	public String getCom_denominacion() {return com_denominacion;}
	public void setCom_denominacion(String com_denominacion) {this.com_denominacion = com_denominacion;}
	public String getCom_nombre_propietario() {return com_nombre_propietario;}
	public void setCom_nombre_propietario(String com_nombre_propietario) {this.com_nombre_propietario = com_nombre_propietario;}
	public String getCom_ocupante() {return com_ocupante;}
	public void setCom_ocupante(String com_ocupante) {this.com_ocupante = com_ocupante;}
	public BigDecimal getCom_frente() {return com_frente;}
	public void setCom_frente(BigDecimal com_frente) {this.com_frente = com_frente;}
	public BigDecimal getCom_fondo() {return com_fondo;}
	public void setCom_fondo(BigDecimal com_fondo) {this.com_fondo = com_fondo;}
	public String getCom_local() {return com_local;}
	public void setCom_local(String com_local) {this.com_local = com_local;}
	public String getCom_ruta() {return com_ruta;}
	public void setCom_ruta(String com_ruta) {this.com_ruta = com_ruta;}
	public String getCom_domicilio() {return com_domicilio;}
	public void setCom_domicilio(String com_domicilio) {this.com_domicilio = com_domicilio;}
	public String getCom_colonia() {return com_colonia;}
	public void setCom_colonia(String com_colonia) {this.com_colonia = com_colonia;}
	public String getCom_domicilio_notificaciones() {return com_domicilio_notificaciones;}
	public void setCom_domicilio_notificaciones(String com_domicilio_notificaciones) {this.com_domicilio_notificaciones = com_domicilio_notificaciones;}
	public Integer getCom_ult_eje() {return com_ult_eje;}
	public void setCom_ult_eje(Integer com_ult_eje) {this.com_ult_eje = com_ult_eje;}
	public String getCom_horario() {return com_horario;}
	public void setCom_horario(String com_horario) {this.com_horario = com_horario;}
	public String getCom_status() {return com_status;}
	public void setCom_status(String com_status) {this.com_status = com_status;}
	public String getCom_usuario() {return com_usuario;}
	public void setCom_usuario(String com_usuario) {this.com_usuario = com_usuario;}
	public String getGiros() {return giros;}
	public void setGiros(String giros) {this.giros = giros;}
	public String getCom_niv() {return com_niv;}
	public void setCom_niv(String com_niv) {this.com_niv = com_niv;}
	public String getCom_num_placa() {return com_num_placa;}
	public void setCom_num_placa(String com_num_placa) {this.com_num_placa = com_num_placa;}
	public String getCom_num_tarj_circulacion() {return com_num_tarj_circulacion;}
	public void setCom_num_tarj_circulacion(String com_num_tarj_circulacion) {this.com_num_tarj_circulacion = com_num_tarj_circulacion;}
	public String getNombreRuta() {return nombreRuta;}
	public void setNombreRuta(String nombreRuta) {this.nombreRuta = nombreRuta;}
	public BigDecimal getTarifa() {return tarifa;}
	public void setTarifa(BigDecimal tarifa) {this.tarifa = tarifa;}
	public String getUltFechaPago() {return ultFechaPago;}
	public void setUltFechaPago(String ultFechaPago) {this.ultFechaPago = ultFechaPago;}
	public Integer getAgente() {return agente;}
	public void setAgente(Integer agente) {this.agente = agente;}

	@Override
	public String toString() {
		return "InComercios{" +
				"id=" + id +
				", com_orden=" + com_orden +
				", com_contribuyente=" + com_contribuyente +
				", com_denominacion='" + com_denominacion + '\'' +
				", com_nombre_propietario='" + com_nombre_propietario + '\'' +
				", com_ocupante='" + com_ocupante + '\'' +
				", com_frente=" + com_frente +
				", com_fondo=" + com_fondo +
				", com_local='" + com_local + '\'' +
				", com_ruta='" + com_ruta + '\'' +
				", com_domicilio='" + com_domicilio + '\'' +
				", com_colonia='" + com_colonia + '\'' +
				", com_domicilio_notificaciones='" + com_domicilio_notificaciones + '\'' +
				", com_ult_eje=" + com_ult_eje +
				", com_horario='" + com_horario + '\'' +
				", com_status='" + com_status + '\'' +
				", com_usuario='" + com_usuario + '\'' +
				", giros='" + giros + '\'' +
				", com_niv='" + com_niv + '\'' +
				", com_num_placa='" + com_num_placa + '\'' +
				", com_num_tarj_circulacion='" + com_num_tarj_circulacion + '\'' +
				", ultFechaPago='" + ultFechaPago + '\'' +
				", nombreRuta='" + nombreRuta + '\'' +
				", tarifa=" + tarifa +
				'}';
	}
}



class ComerciosId implements Serializable{
	private static final long serialVersionUID = 1L;

	Integer com_empresa;
	String  com_tipo;
	Integer com_control;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((com_control == null) ? 0 : com_control.hashCode());
		result = prime * result + ((com_empresa == null) ? 0 : com_empresa.hashCode());
		result = prime * result + ((com_tipo == null) ? 0 : com_tipo.hashCode());
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
		ComerciosId other = (ComerciosId) obj;
		if (com_control == null) {
			if (other.com_control != null)
				return false;
		} else if (!com_control.equals(other.com_control))
			return false;
		if (com_empresa == null) {
			if (other.com_empresa != null)
				return false;
		} else if (!com_empresa.equals(other.com_empresa))
			return false;
		if (com_tipo == null) {
			if (other.com_tipo != null)
				return false;
		} else if (!com_tipo.equals(other.com_tipo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ComerciosId [com_empresa=" + com_empresa + ", com_tipo=" + com_tipo + ", com_control=" + com_control
				+ "]";
	}



}