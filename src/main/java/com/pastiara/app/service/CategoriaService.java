package com.pastiara.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.CategoriaResponseDTO;
import com.pastiara.app.dto.CategoriaSimpleDTO;
import com.pastiara.app.dto.ProductoResponseDTO;
import com.pastiara.app.model.Categoria;
import com.pastiara.app.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    private CategoriaResponseDTO convertirCategoriaADTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        // ❌ No se mapea 'descripcion' en el DTO

        // Creamos un DTO Simple de esta categoría para pasarlo a los productos
        CategoriaSimpleDTO catSimpleDto = new CategoriaSimpleDTO();
        catSimpleDto.setId(categoria.getId());
        catSimpleDto.setNombre(categoria.getNombre());

        // Convertimos la lista de productos anidados
        List<ProductoResponseDTO> productosDto = categoria.getProductos().stream()
            .map(producto -> {
                // Convertimos cada producto manualmente
                ProductoResponseDTO pDto = new ProductoResponseDTO();
                pDto.setId(producto.getId());
                pDto.setNombre(producto.getNombre());
                pDto.setDescripcion(producto.getDescripcion());
                pDto.setPrecio(producto.getPrecio());
                pDto.setImagenUrl(producto.getImagenUrl());
                pDto.setCategoria(catSimpleDto); // Asignamos el DTO simple
                return pDto;
            })
            .collect(Collectors.toList());

        dto.setProductos(productosDto);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
            .map(this::convertirCategoriaADTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return convertirCategoriaADTO(categoria);
    }

    // ⭐ CAMBIO 5: Método 'crear' ajustado para NO recibir descripcion
    @Transactional
    public CategoriaResponseDTO crear(String nombre) {
        Categoria nuevaCategoria = new Categoria(nombre);
        categoriaRepository.save(nuevaCategoria);
        return convertirCategoriaADTO(nuevaCategoria);
    }
 
    // (Solo para ADMIN)
    // ⭐ CAMBIO 6: Método 'actualizar' ajustado para NO recibir descripcion
    @Transactional
    public Categoria actualizar(Long id, String nuevoNombre) {
        // 1. Buscar la categoría existente.
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada para actualizar"));

        // 2. Actualizar los campos
        categoriaExistente.setNombre(nuevoNombre);
        
        // 3. Guardar la entidad actualizada
        return categoriaRepository.save(categoriaExistente);
    }
    
    // (Solo para ADMIN)
    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}