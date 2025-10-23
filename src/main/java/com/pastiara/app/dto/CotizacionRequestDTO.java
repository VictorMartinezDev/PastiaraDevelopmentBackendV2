package com.pastiara.app.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CotizacionRequestDTO {
    // La solicitud JSON contendrá estos 4 campos:
    
    private DireccionEnvioDTO direccion; // La nueva dirección
    private String tipoDeEvento;
    private String comentarios;
    private Set<DetalleCotizacionDTO> detalles; // La lista de productos y cantidades
    
    public CotizacionRequestDTO() {}

	public CotizacionRequestDTO(DireccionEnvioDTO direccion, String tipoDeEvento, String comentarios,
			Set<DetalleCotizacionDTO> detalles) {
		super();
		this.direccion = direccion;
		this.tipoDeEvento = tipoDeEvento;
		this.comentarios = comentarios;
		this.detalles = detalles;
	}

	public DireccionEnvioDTO getDireccion() {
		return direccion;
	}

	public void setDireccion(DireccionEnvioDTO direccion) {
		this.direccion = direccion;
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

	public Set<DetalleCotizacionDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<DetalleCotizacionDTO> detalles) {
		this.detalles = detalles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CotizacionRequestDTO [direccion=");
		builder.append(direccion);
		builder.append(", tipoDeEvento=");
		builder.append(tipoDeEvento);
		builder.append(", comentarios=");
		builder.append(comentarios);
		builder.append(", detalles=");
		builder.append(detalles);
		builder.append("]");
		return builder.toString();
	}
    
    
    
}
