package com.pastiara.app.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "direcciones_envio")
@Data
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String calle;
    @Column(nullable = false)
    private String colonia;
    @Column(nullable = false)
    private String municipio;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String codigoPostal;

    // --- Relación N:1 con Usuario ---
    // 'nullable = true' para permitir invitados
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false) 
    private Usuario usuario;
    
    public DireccionEnvio() {}

	public DireccionEnvio(String calle, String colonia, String municipio, String estado, String codigoPostal) {
		super();
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DireccionEnvio [id=");
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
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DireccionEnvio other = (DireccionEnvio) obj;
		return Objects.equals(id, other.id);
	}
    
    
}
