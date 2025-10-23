package com.pastiara.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastiara.app.model.Cotizacion;

import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    // Para que un administrador vea las cotizaciones de un usuario específico
    List<Cotizacion> findByUsuarioId(Long usuarioId);
}
