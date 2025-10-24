package com.pastiara.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.ProductoResponseDTO;
import com.pastiara.app.model.Producto;
import com.pastiara.app.service.FavoritoService;

import java.util.Set;

@RestController
@RequestMapping("/api/favoritos")
@PreAuthorize("hasRole('CLIENTE')") // Solo para clientes
public class FavoritoController {

    private final FavoritoService favoritosService;

    public FavoritoController(FavoritoService favoritosService) {
        this.favoritosService = favoritosService;
    }

    // CLIENTE: Ver mi lista de favoritos
    @GetMapping
    public ResponseEntity<Set<ProductoResponseDTO>> obtenerMisFavoritos() {
        return ResponseEntity.ok(favoritosService.obtenerMisFavoritos());
    }

    // CLIENTE: Agregar un producto a favoritos
    @PostMapping("/{productoId}")
    public ResponseEntity<Void> agregarAFavoritos(@PathVariable("productoId") Long productoId) {
        favoritosService.agregarAFavoritos(productoId);
        return ResponseEntity.ok().build();
    }

    // CLIENTE: Quitar un producto de favoritos
    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> quitarDeFavoritos(@PathVariable Long productoId) {
        favoritosService.quitarDeFavoritos(productoId);
        return ResponseEntity.noContent().build();
    }
}