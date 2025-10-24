package com.pastiara.app.dto;

import java.util.List;
import java.util.Objects;

public class CategoriaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    
    // Anidamos la lista de DTOs de producto para evitar bucles
    private List<ProductoResponseDTO> productos;
    
    public CategoriaResponseDTO() {}

	public CategoriaResponseDTO(Long id, String nombre, String descripcion, List<ProductoResponseDTO> productos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.productos = productos;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ProductoResponseDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoResponseDTO> productos) {
		this.productos = productos;
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
		CategoriaResponseDTO other = (CategoriaResponseDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CategoriaResponseDTO [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append("]");
		return builder.toString();
	}
    
    
}