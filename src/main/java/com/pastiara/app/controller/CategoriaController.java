package com.pastiara.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.CategoriaDTO;
import com.pastiara.app.dto.CategoriaResponseDTO;
import com.pastiara.app.model.Categoria;
import com.pastiara.app.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // PÚBLICO: Todos pueden ver las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable ("id") Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        CategoriaResponseDTO nueva = categoriaService.crear(
            categoriaDTO.getNombre() 
            // ❌ CAMBIO 4: Eliminamos getDescripcion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    
    // ADMIN: Solo el admin puede actualizar
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> actualizarCategoria(
    		@PathVariable ("id")Long id, 
            @RequestBody CategoriaDTO categoriaDTO) {
        
        Categoria actualizada = categoriaService.actualizar(
            id,
            categoriaDTO.getNombre()
            // ❌ CAMBIO 4: Eliminamos getDescripcion()
        );
        // Retorna 200 OK con la entidad actualizada
        return ResponseEntity.ok(actualizada); 
    }
    
    // ADMIN: Solo el admin puede borrar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable ("id")Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}