package com.pastiara.app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoCreateDTO {
    // Todos los campos de la entidad Producto
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    
    // El ID de la categoría a la que pertenece
    private Long categoriaId;
    
    public ProductoCreateDTO() {}

	public ProductoCreateDTO(String nombre, String descripcion, BigDecimal precio, String imagenUrl, Long categoriaId) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
		this.categoriaId = categoriaId;
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

	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductoCreateDTO [nombre=");
		builder.append(nombre);
		builder.append(", descripcion=");
		builder.append(descripcion);
		builder.append(", precio=");
		builder.append(precio);
		builder.append(", imagenUrl=");
		builder.append(imagenUrl);
		builder.append(", categoriaId=");
		builder.append(categoriaId);
		builder.append("]");
		return builder.toString();
	}
    
    
    
    
}