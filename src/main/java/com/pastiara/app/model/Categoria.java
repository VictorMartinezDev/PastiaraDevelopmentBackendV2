package com.pastiara.app.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;
    

    // --- Relación 1:N ---
    // Una Categoría tiene muchos Productos.
    // "mappedBy" le dice a JPA que la entidad 'Producto'
    // es la dueña de esta relación (ahí está el 'categoria_id').
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private Set<Producto> productos;
    
    // Contructor vacio que necesita JPA
    public Categoria() {}
    
    //Constructor para inicializar nuestros objetos
	public Categoria(String nombre) {
		super();
		this.nombre = nombre;
		
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
	
	

	public Set<Producto> getProductos() {
		return productos;
	}

	public void setProductos(Set<Producto> productos) {
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
		Categoria other = (Categoria) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Categoria [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append("]");
		return builder.toString();
	}
    
    
}
