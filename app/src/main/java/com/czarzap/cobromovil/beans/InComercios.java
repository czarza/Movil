package com.czarzap.cobromovil.beans;

import java.io.Serializable;
import java.math.BigDecimal;


public class InComercios {
	public ComerciosId id = new ComerciosId();
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
	private String  com_cp;
	private String  com_telefono;
	private String  com_email;
	private String  com_domicilio_notificaciones;
	private String    com_fecha_ingreso;
	private String    com_fecha_baja;
	private Integer com_ult_eje;
	private String  com_horario;
	private String  com_notas;
	private String  com_status;
	private String  com_usuario;
	private String fecha_ingreso;
	private String fecha_baja;
	private String nombreContribuyente;
	private String giros;
	private String anuncios;
	private String  com_ife;
	private String  com_curp;
	private String  com_niv;
	private String  com_num_placa;
	private String  com_num_tarj_circulacion;

	public InComercios() {
		super();
	}

	public InComercios(Integer empresa, String tipo, Integer control) {
		this.setCom_empresa(empresa);
		this.setCom_tipo(tipo);
		this.setCom_control(control);
	}

	public ComerciosId getId() { return id; }
	public void setId(ComerciosId id) { this.id = id; }
	public Integer getCom_empresa() { return id.com_empresa; }
	public void setCom_empresa(Integer com_empresa) { this.id.com_empresa = com_empresa; }
	public String getCom_tipo() { return id.com_tipo; }
	public void setCom_tipo(String com_tipo) { this.id.com_tipo = com_tipo; }
	public Integer getCom_contribuyente() { return com_contribuyente; }
	public void setCom_contribuyente(Integer com_contribuyente) { this.com_contribuyente = com_contribuyente; }
	public Integer getCom_control() { return id.com_control; }
	public void setCom_control(Integer com_control) { this.id.com_control = com_control; }
	public String getCom_denominacion() { return com_denominacion; }
	public void setCom_denominacion(String com_denominacion) { this.com_denominacion = com_denominacion; }
	public String getCom_nombre_propietario() { return com_nombre_propietario; }
	public void setCom_nombre_propietario(String com_nombre_propietario) { this.com_nombre_propietario = com_nombre_propietario; }
	public String getCom_domicilio() { return com_domicilio; }
	public void setCom_domicilio(String com_domicilio) { this.com_domicilio = com_domicilio; }
	public String getCom_colonia() { return com_colonia; }
	public void setCom_colonia(String com_colonia) { this.com_colonia = com_colonia; }
	public String getCom_cp() { return com_cp; }
	public void setCom_cp(String com_cp) { this.com_cp = com_cp; }
	public String getCom_telefono() { return com_telefono; }
	public void setCom_telefono(String com_telefono) { this.com_telefono = com_telefono; }
	public String getCom_email() { return com_email; }
	public void setCom_email(String com_email) { this.com_email = com_email; }
	public String getCom_domicilio_notificaciones() { return com_domicilio_notificaciones; }
	public void setCom_domicilio_notificaciones(String com_domicilio_notificaciones) { this.com_domicilio_notificaciones = com_domicilio_notificaciones; }
	public String getCom_fecha_ingreso() { return com_fecha_ingreso; }
	public void setCom_fecha_ingreso(String com_fecha_ingreso) { this.com_fecha_ingreso = com_fecha_ingreso; }
	public String getCom_fecha_baja() { return com_fecha_baja; }
	public void setCom_fecha_baja(String com_fecha_baja) { this.com_fecha_baja = com_fecha_baja; }
	public Integer getCom_ult_eje() { return com_ult_eje; }
	public void setCom_ult_eje(Integer com_ult_eje) { this.com_ult_eje = com_ult_eje; }
	public String getCom_horario() { return com_horario; }
	public void setCom_horario(String com_horario) { this.com_horario = com_horario; }
	public String getCom_notas() { return com_notas; }
	public void setCom_notas(String com_notas) { this.com_notas = com_notas; }
	public String getCom_status() { return com_status; }
	public void setCom_status(String com_status) { this.com_status = com_status; }
	public String getCom_usuario() { return com_usuario; }
	public void setCom_usuario(String com_usuario) { this.com_usuario = com_usuario; }
	public String getCom_ruta() { return com_ruta; }
	public String getNombreContribuyente() { return nombreContribuyente; }
	public void setNombreContribuyente(String nombreContribuyente) { this.nombreContribuyente = nombreContribuyente; }
	public String getGiros() { return giros; }
	public void setGiros(String giros) { this.giros = giros; }
	public String getAnuncios() { return anuncios; }
	public void setAnuncios(String anuncios) { this.anuncios = anuncios; }
	public void setCom_ruta(String com_ruta) { this.com_ruta = com_ruta; }
	public String getCom_ocupante() { return com_ocupante; }
	public void setCom_ocupante(String com_ocupante) { this.com_ocupante = com_ocupante; }
	public BigDecimal getCom_frente() { return com_frente; }
	public void setCom_frente(BigDecimal com_frente) { this.com_frente = com_frente; }
	public BigDecimal getCom_fondo() { return com_fondo; }
	public void setCom_fondo(BigDecimal com_fondo) { this.com_fondo = com_fondo; }
	public String getCom_local() { return com_local; }
	public void setCom_local(String com_local) { this.com_local = com_local; }
	public String getFecha_ingreso() {return fecha_ingreso;}
	public void setFecha_ingreso(String fecha_ingreso) {this.fecha_ingreso = fecha_ingreso;}
	public String getFecha_baja() {return fecha_baja;}
	public void setFecha_baja(String fecha_baja) {this.fecha_baja = fecha_baja;}
	public String getCom_ife() {return com_ife;}
	public void setCom_ife(String com_ife) {this.com_ife = com_ife;}
	public String getCom_curp() {return com_curp;}
	public void setCom_curp(String com_curp) {this.com_curp = com_curp;}
	public String getCom_niv() {return com_niv;}
	public void setCom_niv(String com_niv) {this.com_niv = com_niv;}
	public String getCom_num_placa() {return com_num_placa;}
	public void setCom_num_placa(String com_num_placa) {this.com_num_placa = com_num_placa;}
	public String getCom_num_tarj_circulacion() {return com_num_tarj_circulacion;}
	public void setCom_num_tarj_circulacion(String com_num_tarj_circulacion) {this.com_num_tarj_circulacion = com_num_tarj_circulacion;}

	@Override
	public String toString() {
		return "InComercios{" +
				"id=" + id +
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
				", com_cp='" + com_cp + '\'' +
				", com_telefono='" + com_telefono + '\'' +
				", com_email='" + com_email + '\'' +
				", com_domicilio_notificaciones='" + com_domicilio_notificaciones + '\'' +
				", com_fecha_ingreso='" + com_fecha_ingreso + '\'' +
				", com_fecha_baja='" + com_fecha_baja + '\'' +
				", com_ult_eje=" + com_ult_eje +
				", com_horario='" + com_horario + '\'' +
				", com_notas='" + com_notas + '\'' +
				", com_status='" + com_status + '\'' +
				", com_usuario='" + com_usuario + '\'' +
				", fecha_ingreso='" + fecha_ingreso + '\'' +
				", fecha_baja='" + fecha_baja + '\'' +
				", nombreContribuyente='" + nombreContribuyente + '\'' +
				", giros='" + giros + '\'' +
				", anuncios='" + anuncios + '\'' +
				", com_ife='" + com_ife + '\'' +
				", com_curp='" + com_curp + '\'' +
				", com_niv='" + com_niv + '\'' +
				", com_num_placa='" + com_num_placa + '\'' +
				", com_num_tarj_circulacion='" + com_num_tarj_circulacion + '\'' +
				'}';
	}
}

class ComerciosId implements Serializable{
	private static final long serialVersionUID = 1L;

	Integer com_empresa;
	String  com_tipo;
	Integer com_control;

	public Integer getCom_empresa() { return com_empresa; }
	public void setCom_empresa(Integer com_empresa) { this.com_empresa = com_empresa; }
	public Integer getCom_control() { return com_control; }
	public void setCom_control(Integer com_control) { this.com_control = com_control; }
	public String getCom_tipo() { return com_tipo; }
	public void setCom_tipo(String com_tipo) { this.com_tipo = com_tipo; }
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