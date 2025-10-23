package com.pastiara.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true, length= 10)
    private String numeroTelefono;

    @Column(nullable = false, unique = true)
    private String email;
    
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Guarda el rol como "ADMIN" o "CLIENTE"
    @Column(nullable = false)
    private UserRole rol;

    @Column(updatable = false) // Se crea una vez y no se actualiza
    private LocalDateTime fechaRegistro;

    // --- Relación 1:N con Direcciones ---
    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<DireccionEnvio> direcciones;

    // --- Relación 1:N con Cotizaciones ---
    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<Cotizacion> cotizaciones;

    // --- Relación N:M con Productos (FAVORITOS) ---
    // Esta es la forma JPA de crear la tabla pivote "favoritos".
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "favoritos", // Nombre de la tabla pivote
        joinColumns = @JoinColumn(name = "usuario_id"), // Columna de esta entidad
        inverseJoinColumns = @JoinColumn(name = "producto_id") // Columna de la otra entidad
    )
    private Set<Producto> productosFavoritos;

    // Método para asignar la fecha de registro automáticamente
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.rol == null) {
            this.rol = UserRole.CLIENTE; // Asignar rol por defecto
        }
    }
    
    public Usuario() {}

	public Usuario(String nombre, String numeroTelefono, String email, String password, UserRole rol,
			LocalDateTime fechaRegistro) {
		super();
		this.nombre = nombre;
		this.numeroTelefono = numeroTelefono;
		this.email = email;
		this.password = password;
		this.rol = rol;
		this.fechaRegistro = fechaRegistro;
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

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
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

	public UserRole getRol() {
		return rol;
	}

	public void setRol(UserRole rol) {
		this.rol = rol;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Set<Producto> getProductosFavoritos() {
		return productosFavoritos;
	}

	public void setProductosFavoritos(Set<Producto> productosFavoritos) {
		this.productosFavoritos = productosFavoritos;
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
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Usuario [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", numeroTelefono=");
		builder.append(numeroTelefono);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}
    
}