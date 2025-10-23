package com.pastiara.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastiara.app.model.DireccionEnvio;

import java.util.List;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {
    
    // Para mostrarle a un usuario solo SUS direcciones
    List<DireccionEnvio> findByUsuarioId(Long usuarioId);
}