package com.pastiara.app.dto;

import java.util.Objects;

import com.pastiara.app.model.UserRole;

public class PerfilUsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String numeroTelefono;
    private UserRole rol;
    
    public PerfilUsuarioDTO() {}

	public PerfilUsuarioDTO(Long id, String nombre, String email, String numeroTelefono, UserRole rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.numeroTelefono = numeroTelefono;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public UserRole getRol() {
		return rol;
	}

	public void setRol(UserRole rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PerfilUsuarioDTO [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
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
		PerfilUsuarioDTO other = (PerfilUsuarioDTO) obj;
		return Objects.equals(id, other.id);
	}
    
    
}
