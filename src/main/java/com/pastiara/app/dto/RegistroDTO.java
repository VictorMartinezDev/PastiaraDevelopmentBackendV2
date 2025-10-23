package com.pastiara.app.dto;

import lombok.Data;

@Data
public class RegistroDTO {
    private String nombre;
    private String email;
    private String password;
    private String numeroTelefono;
    
    public RegistroDTO() {}

	public RegistroDTO(String nombre, String email, String password, String numeroTelefono) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.numeroTelefono= numeroTelefono;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegistroDTO [nombre=");
		builder.append(nombre);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
    
    
    
}
