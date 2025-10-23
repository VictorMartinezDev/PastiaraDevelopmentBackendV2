package com.pastiara.app.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    
    // Anidamos el DTO simple, no la entidad
    private CategoriaSimpleDTO categoria; 
    
    public ProductoResponseDTO() {}

	public ProductoResponseDTO(Long id, String nombre, String descripcion, BigDecimal precio, String imagenUrl,
			CategoriaSimpleDTO categoria) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
		this.categoria = categoria;
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

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	public CategoriaSimpleDTO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaSimpleDTO categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoResponseDTO other = (ProductoResponseDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
	}
    
    
}