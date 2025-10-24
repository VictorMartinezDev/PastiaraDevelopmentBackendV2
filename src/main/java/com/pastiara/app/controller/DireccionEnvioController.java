package com.pastiara.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.DireccionEnvioDTO;
import com.pastiara.app.model.DireccionEnvio;
import com.pastiara.app.service.DireccionEnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@PreAuthorize("hasRole('CLIENTE')") // ¡Seguridad a nivel de clase!
public class DireccionEnvioController {

    private final DireccionEnvioService direccionEnvioService;

    public DireccionEnvioController(DireccionEnvioService direccionEnvioService) {
        this.direccionEnvioService = direccionEnvioService;
    }

    // CLIENTE: Ver mis direcciones
    @GetMapping
    public ResponseEntity<List<DireccionEnvio>> obtenerMisDirecciones() {
        return ResponseEntity.ok(direccionEnvioService.obtenerMisDirecciones());
    }

    // CLIENTE: Crear una nueva dirección
    @PostMapping
    public ResponseEntity<DireccionEnvio> crearDireccion(@RequestBody DireccionEnvioDTO dto) {
        // Convertimos DTO a Entidad
        DireccionEnvio direccion = new DireccionEnvio();
        direccion.setCalle(dto.getCalle());
        direccion.setColonia(dto.getColonia());
        direccion.setMunicipio(dto.getMunicipio());
        direccion.setEstado(dto.getEstado());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        
        DireccionEnvio nueva = direccionEnvioService.crearNuevaDireccion(direccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
 // CLIENTE: Modificar una de mis direcciones
    @PutMapping("/{id}")
    public ResponseEntity<DireccionEnvio> actualizarDireccion(
            @PathVariable("id") Long id, 
            @RequestBody DireccionEnvioDTO dto) {
        
        DireccionEnvio direccionActualizada = direccionEnvioService.actualizarDireccion(id, dto);
        return ResponseEntity.ok(direccionActualizada);
    }
    // CLIENTE: Borrar una de mis direcciones
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable ("id") Long id) {
        direccionEnvioService.eliminarDireccion(id);
        return ResponseEntity.noContent().build();
    }
}