package com.pastiara.app.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class DetalleCotizacionResponseDTO {
    private Long id;
    private int cantidad;
    private BigDecimal precioUnitarioCotizado;
    private Long productoId;
    private String nombreProducto; // <-- Solo el nombre
    
    public DetalleCotizacionResponseDTO() {}

	public DetalleCotizacionResponseDTO(Long id, int cantidad, BigDecimal precioUnitarioCotizado, Long productoId,
			String nombreProducto) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.precioUnitarioCotizado = precioUnitarioCotizado;
		this.productoId = productoId;
		this.nombreProducto = nombreProducto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitarioCotizado() {
		return precioUnitarioCotizado;
	}

	public void setPrecioUnitarioCotizado(BigDecimal precioUnitarioCotizado) {
		this.precioUnitarioCotizado = precioUnitarioCotizado;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
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
		DetalleCotizacionResponseDTO other = (DetalleCotizacionResponseDTO) obj;
		return Objects.equals(id, other.id);
	}
    
    
}