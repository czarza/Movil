package com.czarzap.cobromovil.beans;

import java.io.Serializable;
import java.math.BigDecimal;


public class InComercio_cobro_movil {
	private InComercio_nofijo_pagosPK id = new InComercio_nofijo_pagosPK();
	 private BigDecimal cac_total;
	 private String cac_fecha_pago;
	 private String cac_agente;
	 private String cac_notas;
	 private String fecha_pago;
	 private String propietario;
	 private String ruta;
	 private String nombreAgente;

	public InComercio_cobro_movil() {
		super();
	}

	public InComercio_cobro_movil(Integer cac_empresa, String cac_tipo, Integer cac_control, Integer cac_numero_pago) {
		super();
		this.setCac_empresa(cac_empresa);
		this.setCac_tipo(cac_tipo);
		this.setCac_control(cac_control);
		this.setCac_numero_pago(cac_numero_pago);
	}


	public InComercio_nofijo_pagosPK getId() { return id; }
	public void setId(InComercio_nofijo_pagosPK id) { this.id = id; }
	public Integer getCac_empresa() { return id.cac_empresa; }
	public void setCac_empresa(Integer cac_empresa) { this.id.cac_empresa = cac_empresa; }
	public String getCac_tipo() { return id.cac_tipo; }
	public void setCac_tipo(String cac_tipo) { this.id.cac_tipo = cac_tipo; }
	public Integer getCac_control() { return id.cac_control; }
	public void setCac_control(Integer cac_control) { this.id.cac_control = cac_control; }
	public Integer getCac_numero_pago() { return id.cac_numero_pago; }
	public void setCac_numero_pago(Integer cac_numero_pago) { this.id.cac_numero_pago = cac_numero_pago; }
	public BigDecimal getCac_total() { return cac_total; }
	public void setCac_total(BigDecimal cac_total) { this.cac_total = cac_total; }
	public String getCac_fecha_pago() { return cac_fecha_pago; }
	public void setCac_fecha_pago(String cac_fecha_pago) { this.cac_fecha_pago = cac_fecha_pago; }
	public String getCac_agente() { return cac_agente; }
	public void setCac_agente(String cac_agente) { this.cac_agente = cac_agente; }
	public String getCac_notas() { return cac_notas; }
	public void setCac_notas(String cac_notas) { this.cac_notas = cac_notas; }
	public String getPropietario() { return propietario; }
	public void setPropietario(String propietario) { this.propietario = propietario; }
	public String getRuta() { return ruta; }
	public void setRuta(String ruta) { this.ruta = ruta; }
	public String getNombreAgente() { return nombreAgente; }
	public void setNombreAgente(String nombreAgente) { this.nombreAgente = nombreAgente; }
	public void setFecha_pago(String fecha_pago) { this.fecha_pago = fecha_pago; }
	public String getFecha_pago() {return fecha_pago;}

	@Override
	public String toString() {
		return "InComercio_cobro_movil [id=" + id.toString() + ", cac_total=" + cac_total + ", cac_fecha_pago=" + cac_fecha_pago
				+ ", cac_agente=" + cac_agente + ", cac_notas=" + cac_notas + ", fecha_pago=" + fecha_pago
				+ ", propietario=" + propietario + ", ruta=" + ruta + ", nombreAgente=" + nombreAgente + "]";
	}


}

class InComercio_nofijo_pagosPK implements Serializable{
	private static final long serialVersionUID = 1L;
	Integer cac_empresa;
	String  cac_tipo;
	Integer cac_control;
	Integer cac_numero_pago;

	public Integer getCac_empresa() { return cac_empresa; }
	public void setCac_empresa(Integer cac_empresa) { this.cac_empresa = cac_empresa; }
	public String getCac_tipo() { return cac_tipo; }
	public void setCac_tipo(String cac_tipo) { this.cac_tipo = cac_tipo; }
	public Integer getCac_control() { return cac_control; }
	public void setCac_control(Integer cac_control) { this.cac_control = cac_control; }
	public Integer getCac_numero_pago() { return cac_numero_pago; }
	public void setCac_numero_pago(Integer cac_numero_pago) { this.cac_numero_pago = cac_numero_pago; }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cac_control == null) ? 0 : cac_control.hashCode());
		result = prime * result + ((cac_empresa == null) ? 0 : cac_empresa.hashCode());
		result = prime * result + ((cac_numero_pago == null) ? 0 : cac_numero_pago.hashCode());
		result = prime * result + ((cac_tipo == null) ? 0 : cac_tipo.hashCode());
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
		InComercio_nofijo_pagosPK other = (InComercio_nofijo_pagosPK) obj;
		if (cac_control == null) {
			if (other.cac_control != null)
				return false;
		} else if (!cac_control.equals(other.cac_control))
			return false;
		if (cac_empresa == null) {
			if (other.cac_empresa != null)
				return false;
		} else if (!cac_empresa.equals(other.cac_empresa))
			return false;
		if (cac_numero_pago == null) {
			if (other.cac_numero_pago != null)
				return false;
		} else if (!cac_numero_pago.equals(other.cac_numero_pago))
			return false;
		if (cac_tipo == null) {
			if (other.cac_tipo != null)
				return false;
		} else if (!cac_tipo.equals(other.cac_tipo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "InComercio_nofijo_pagosPK [cac_empresa=" + cac_empresa + ", cac_tipo=" + cac_tipo + ", cac_control="
				+ cac_control + ", cac_numero_pago=" + cac_numero_pago + "]";
	}


}