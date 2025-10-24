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
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // Necesario para crear/actualizar

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }


    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        
        // 1. Busca la entidad en la base de datos
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        // 2. Convierte la entidad a DTO usando el método auxiliar
        // Esto es seguro porque estamos dentro de la transacción.
        return convertirProductoADTO(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerPorCategoria(Long categoriaId) {
        
        // 1. Llama al repositorio para buscar por ID de categoría
        // (Este método 'findByCategoriaId' lo creamos en tu ProductoRepository)
        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
        
        // 2. Convierte la lista de Entidades a DTOs (reutilizando el método)
        // Esto es seguro porque estamos dentro de la transacción.
        return productos.stream()
            .map(this::convertirProductoADTO)
            .collect(Collectors.toList());
    }
    
    private ProductoResponseDTO convertirProductoADTO(Producto productoGuardado) {
        // 1. Convertir la Categoria anidada
        CategoriaSimpleDTO catDto = new CategoriaSimpleDTO();
        catDto.setId(productoGuardado.getCategoria().getId());
        catDto.setNombre(productoGuardado.getCategoria().getNombre());
        
        // 2. Convertir el Producto principal
        ProductoResponseDTO responseDto = new ProductoResponseDTO();
        responseDto.setId(productoGuardado.getId());
        responseDto.setNombre(productoGuardado.getNombre());
        responseDto.setDescripcion(productoGuardado.getDescripcion());
        responseDto.setPrecio(productoGuardado.getPrecio());
        responseDto.setImagenUrl(productoGuardado.getImagenUrl());
        responseDto.setCategoria(catDto); // Asignamos el DTO anidado
        
        return responseDto;
    }
    
    

    // (Solo para ADMIN)
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

        return convertirProductoADTO(productoGuardado);
    } // <-- Este es el final del método de GUARDADO/CREACIÓN
    
 
    
    @Transactional(readOnly = true) // readOnly = true optimiza las consultas de solo lectura
    public List<ProductoResponseDTO> obtenerTodos() {
        
        // 1. Obtiene todas las entidades de la BD
        List<Producto> productos = productoRepository.findAll();
        
        // 2. Convierte la lista de Entidades a una lista de DTOs
        // (Esto ocurre DENTRO de la transacción, por lo que es seguro)
        return productos.stream()
            .map(this::convertirProductoADTO) // Reutiliza el método auxiliar
            .collect(Collectors.toList());
    } 
    
   
    // MÉTODO- ACTUALIZAR PRODUCTO (para el PUT)
    
    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoCreateDTO dto) {
        // 1. Buscar el producto existente.
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado para actualizar"));

        // 2. Buscar la entidad Categoria (si el ID de categoría se proporciona para el cambio)
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // 3. Actualizar los campos del producto existente
        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setPrecio(dto.getPrecio());
        productoExistente.setImagenUrl(dto.getImagenUrl());
        productoExistente.setCategoria(categoria); // Actualizamos la relación
        
        // 4. Guardar la entidad actualizada (se guarda automáticamente por @Transactional, pero lo hacemos explícito)
        Producto productoActualizado = productoRepository.save(productoExistente);
        
        // 5. Devolver el DTO de respuesta (Conversión de Entidad a DTO)
        CategoriaSimpleDTO catDto = new CategoriaSimpleDTO(
            productoActualizado.getCategoria().getId(),
            productoActualizado.getCategoria().getNombre()
        );
            
        ProductoResponseDTO responseDto = new ProductoResponseDTO();
        responseDto.setId(productoActualizado.getId());
        responseDto.setNombre(productoActualizado.getNombre());
        responseDto.setDescripcion(productoActualizado.getDescripcion());
        responseDto.setPrecio(productoActualizado.getPrecio());
        responseDto.setImagenUrl(productoActualizado.getImagenUrl());
        responseDto.setCategoria(catDto);
        
        return responseDto;
    }
    
    // (Solo para ADMIN)
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}