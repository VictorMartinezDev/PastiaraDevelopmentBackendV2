package com.pastiara.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.CotizacionRequestDTO;
import com.pastiara.app.dto.CotizacionResponseDTO;
import com.pastiara.app.model.Cotizacion;
import com.pastiara.app.service.CotizacionService;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@PreAuthorize("hasRole('CLIENTE')") // Solo para clientes
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    // CLIENTE: Crear una nueva cotización
    @PostMapping
    // ¡CAMBIO! El tipo de respuesta ahora es DTO
    public ResponseEntity<CotizacionResponseDTO> crearCotizacion(
            @RequestBody CotizacionRequestDTO requestDTO
    ) {
        // El servicio ahora hace todo el trabajo
        CotizacionResponseDTO responseDto = cotizacionService.crearNuevaCotizacion(requestDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // CLIENTE: Ver mi historial de cotizaciones
    @GetMapping
    public ResponseEntity<List<Cotizacion>> obtenerMisCotizaciones() {
        return ResponseEntity.ok(cotizacionService.obtenerMisCotizaciones());
    }
}