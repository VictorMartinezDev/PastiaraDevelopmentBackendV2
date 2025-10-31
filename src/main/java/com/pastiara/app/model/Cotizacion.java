package com.pastiara.app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tipoDeEvento;
    
    @Column 
    private LocalDate fechaEvento;
    
    @Lob
    private String comentarios;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalCotizado;

    // --- Relación N:1 con Usuario ---
    // 'nullable = true' para permitir invitados
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- Relación N:1 con DireccionEnvio ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_envio_id", nullable = false)
    private DireccionEnvio direccionEnvio;

    // --- Relación 1:N con DetalleCotizacion ---
    // "CascadeType.ALL" significa que si guardas/borras una cotización,
    // también se guardan/borran sus detalles.
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Cotizacion() {}

	public Cotizacion(String tipoDeEvento, String comentarios, LocalDateTime fechaCreacion, BigDecimal totalCotizado) {
		super();
		this.tipoDeEvento = tipoDeEvento;
		this.comentarios = comentarios;
		this.fechaCreacion = fechaCreacion;
		this.totalCotizado = totalCotizado;
	}

	public LocalDate getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(LocalDate fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoDeEvento() {
		return tipoDeEvento;
	}

	public void setTipoDeEvento(String tipoDeEvento) {
		this.tipoDeEvento = tipoDeEvento;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public BigDecimal getTotalCotizado() {
		return totalCotizado;
	}

	public void setTotalCotizado(BigDecimal totalCotizado) {
		this.totalCotizado = totalCotizado;
	}

	public DireccionEnvio getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(DireccionEnvio direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<DetalleCotizacion> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleCotizacion> detalles) {
		this.detalles = detalles;
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
		Cotizacion other = (Cotizacion) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cotizacion [id=");
		builder.append(id);
		builder.append(", tipoDeEvento=");
		builder.append(tipoDeEvento);
		builder.append(", comentarios=");
		builder.append(comentarios);
		builder.append(", fechaCreacion=");
		builder.append(fechaCreacion);
		builder.append(", totalCotizado=");
		builder.append(totalCotizado);
		builder.append("]");
		return builder.toString();
	}
    
    
}