package com.pastiara.app.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detalle_cotizacion")
@Data
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioCotizado;

    // --- Relación N:1 con Cotizacion ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private Cotizacion cotizacion;

    // --- Relación N:1 con Producto ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    public DetalleCotizacion() {}

	public DetalleCotizacion(Long id, int cantidad, BigDecimal precioUnitarioCotizado) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.precioUnitarioCotizado = precioUnitarioCotizado;
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

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DetalleCotizacion [id=");
		builder.append(id);
		builder.append(", cantidad=");
		builder.append(cantidad);
		builder.append("]");
		return builder.toString();
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
		DetalleCotizacion other = (DetalleCotizacion) obj;
		return Objects.equals(id, other.id);
	}
    
    
}