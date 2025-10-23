package com.pastiara.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.CategoriaSimpleDTO;
import com.pastiara.app.dto.ProductoCreateDTO;
import com.pastiara.app.dto.ProductoResponseDTO;
import com.pastiara.app.model.Categoria;
import com.pastiara.app.model.Producto;
import com.pastiara.app.repository.CategoriaRepository;
import com.pastiara.app.repository.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // Necesario para crear/actualizar

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    // (Solo para ADMIN)
 // Ahora recibe el DTO de creación y devuelve el DTO de respuesta
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoCreateDTO dto) {
        
        // 1. Buscar la entidad Categoria
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 2. Convertir DTO a Entidad para guardar
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria); // Asignamos la entidad

        // 3. Guardar la entidad
        Producto productoGuardado = productoRepository.save(producto);

        // 4. --- ¡CONVERSIÓN DENTRO DE LA TRANSACCIÓN! ---
        // Aquí SÍ podemos acceder a .getNombre() porque la sesión está abierta.
        CategoriaSimpleDTO catDto = new CategoriaSimpleDTO();
        catDto.setId(productoGuardado.getCategoria().getId());
        catDto.setNombre(productoGuardado.getCategoria().getNombre());
        
        ProductoResponseDTO responseDto = new ProductoResponseDTO();
        responseDto.setId(productoGuardado.getId());
        responseDto.setNombre(productoGuardado.getNombre());
        responseDto.setDescripcion(productoGuardado.getDescripcion());
        responseDto.setPrecio(productoGuardado.getPrecio());
        responseDto.setImagenUrl(productoGuardado.getImagenUrl());
        responseDto.setCategoria(catDto);
        
        // 5. Devolver el DTO limpio
        return responseDto;
    }
    // (Solo para ADMIN)
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}