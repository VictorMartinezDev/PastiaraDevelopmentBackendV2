package com.pastiara.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pastiara.app.dto.ProductoCreateDTO;
import com.pastiara.app.dto.ProductoResponseDTO;
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
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        // Llama al servicio, que ahora devuelve DTOs
        List<ProductoResponseDTO> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    // PÚBLICO: Ver un producto
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable ("id") Long id) {
        
        // Llama al servicio, que ahora devuelve un DTO
        ProductoResponseDTO producto = productoService.obtenerPorId(id);
        
        return ResponseEntity.ok(producto);
        }

    // PÚBLICO: Ver productos de una categoría
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerPorCategoria(@PathVariable ("id") Long id) {
        
        // Llama al servicio (que ahora devuelve DTOs)
        List<ProductoResponseDTO> productos = productoService.obtenerPorCategoria(id);
        
        return ResponseEntity.ok(productos);
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
    public ResponseEntity<Void> eliminarProducto(@PathVariable ("id") Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
    
 // ADMIN: Actualizar un producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable("id") Long id, 
            @RequestBody ProductoCreateDTO dto) {
        
        ProductoResponseDTO responseDto = productoService.actualizarProducto(id, dto);
        
        // 200 OK es la respuesta estándar para PUT exitoso
        return ResponseEntity.ok(responseDto);
    }
}