package com.pastiara.app.dto;

import lombok.Data;

@Data
public class DireccionEnvioDTO {
    // Solo los campos que el usuario llena en el formulario
    private String calle;
    private String colonia;
    private String municipio;
    private String estado;
    private String codigoPostal;
    
    public DireccionEnvioDTO() {}

	public DireccionEnvioDTO(String calle, String colonia, String municipio, String estado, String codigoPostal) {
		super();
		this.calle = calle;
		this.colonia = colonia;
		this.municipio = municipio;
		this.estado = estado;
		this.codigoPostal = codigoPostal;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DireccionEnvioDTO [calle=");
		builder.append(calle);
		builder.append(", colonia=");
		builder.append(colonia);
		builder.append(", municipio=");
		builder.append(municipio);
		builder.append(", estado=");
		builder.append(estado);
		builder.append(", codigoPostal=");
		builder.append(codigoPostal);
		builder.append("]");
		return builder.toString();
	}
    
    
}
