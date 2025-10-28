package com.pastiara.app.dto;


public class CategoriaDTO {
    private String nombre;
    // Eliminamos 'descripcion' para coincidir con la entidad Categoria

    public CategoriaDTO() {
 
    }

	public CategoriaDTO(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CategoriaDTO [nombre=");
		builder.append(nombre);
		builder.append("]");
		return builder.toString();
	}
}