package com.pastiara.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.CategoriaSimpleDTO;
import com.pastiara.app.dto.ProductoCreateDTO;
import com.pastiara.app.dto.ProductoResponseDTO;
import com.pastiara.app.model.Producto;
import com.pastiara.app.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // PÚBLICO: Ver todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // PÚBLICO: Ver un producto
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // PÚBLICO: Ver productos de una categoría
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(id));
    }

    // ADMIN: Crear un producto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody ProductoCreateDTO dto) {
        
        // El controlador ya no convierte nada.
        // Simplemente llama al servicio y devuelve lo que el servicio le da.
        ProductoResponseDTO responseDto = productoService.crearProducto(dto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // ADMIN: Eliminar un producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}