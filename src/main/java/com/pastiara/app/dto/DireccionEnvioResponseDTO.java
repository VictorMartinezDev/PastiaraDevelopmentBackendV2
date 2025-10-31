package com.pastiara.app.dto;

import java.util.Objects;

public class DireccionEnvioResponseDTO {
    
    private Long id;
    private String calle;
    private String colonia;
    private String municipio;
    private String estado;
    private String codigoPostal;
    
    
    public DireccionEnvioResponseDTO() {}


	public DireccionEnvioResponseDTO(Long id, String calle, String colonia, String municipio, String estado,
			String codigoPostal) {
		super();
		this.id = id;
		this.calle = calle;
		this.colonia = colonia;
		this.municipio = municipio;
		this.estado = estado;
		this.codigoPostal = codigoPostal;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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
		builder.append("DireccionEnvioResponseDTO [id=");
		builder.append(id);
		builder.append(", calle=");
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


	@Override
	public int hashCode() {
		return Objects.hash(calle, codigoPostal, colonia, estado, id, municipio);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DireccionEnvioResponseDTO other = (DireccionEnvioResponseDTO) obj;
		return Objects.equals(calle, other.calle) && Objects.equals(codigoPostal, other.codigoPostal)
				&& Objects.equals(colonia, other.colonia) && Objects.equals(estado, other.estado)
				&& Objects.equals(id, other.id) && Objects.equals(municipio, other.municipio);
	}
    
    
    
}
