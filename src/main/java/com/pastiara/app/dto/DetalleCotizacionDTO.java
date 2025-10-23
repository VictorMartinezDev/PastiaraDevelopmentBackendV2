package com.pastiara.app.dto;

import lombok.Data;

@Data // De Lombok: genera getters, setters, etc.
public class DetalleCotizacionDTO {
    // Solo la información que el frontend necesita enviar
    private Long productoId;
    private int cantidad;
    
    public DetalleCotizacionDTO() {}

	public DetalleCotizacionDTO(Long productoId, int cantidad) {
		super();
		this.productoId = productoId;
		this.cantidad = cantidad;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DetalleCotizacionDTO [productoId=");
		builder.append(productoId);
		builder.append(", cantidad=");
		builder.append(cantidad);
		builder.append("]");
		return builder.toString();
	}
    
    
}