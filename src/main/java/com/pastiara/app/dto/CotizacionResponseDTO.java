package com.pastiara.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


import com.pastiara.app.model.DireccionEnvio;

public class CotizacionResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private BigDecimal totalCotizado;
    private String tipoDeEvento;
    private String comentarios;
    private LocalDate fechaEvento;
    
    // Asumiremos que está bien devolver la entidad DireccionEnvio,
    // ya que no tiene bucles de vuelta a Cotizacion.
    private DireccionEnvioResponseDTO direccionEnvio; 
    
    // ¡Usamos el DTO de detalle!
    private List<DetalleCotizacionResponseDTO> detalles;
    
    public CotizacionResponseDTO() {}

	

	public CotizacionResponseDTO(Long id, LocalDateTime fechaCreacion, BigDecimal totalCotizado, String tipoDeEvento,
			String comentarios, DireccionEnvioResponseDTO direccionEnvio, List<DetalleCotizacionResponseDTO> detalles) {
		super();
		this.id = id;
		this.fechaCreacion = fechaCreacion;
		this.totalCotizado = totalCotizado;
		this.tipoDeEvento = tipoDeEvento;
		this.comentarios = comentarios;
		this.direccionEnvio = direccionEnvio;
		this.detalles = detalles;
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

	public DireccionEnvioResponseDTO getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(DireccionEnvioResponseDTO direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public List<DetalleCotizacionResponseDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleCotizacionResponseDTO> detalles) {
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
		CotizacionResponseDTO other = (CotizacionResponseDTO) obj;
		return Objects.equals(id, other.id);
	}
    
    
}