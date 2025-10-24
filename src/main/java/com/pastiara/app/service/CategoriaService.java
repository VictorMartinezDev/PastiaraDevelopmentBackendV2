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

    // Tu método 'crear' ahora debe devolver el DTO
    @Transactional
    public CategoriaResponseDTO crear(String nombre, String descripcion) {
        Categoria nuevaCategoria = new Categoria(nombre);
        categoriaRepository.save(nuevaCategoria);
        // Devolvemos el DTO (la lista de productos estará vacía, lo cual está bien)
        return convertirCategoriaADTO(nuevaCategoria);
    }
 
    // (Solo para ADMIN)
    @Transactional
    public Categoria actualizar(Long id, String nuevoNombre, String nuevaDescripcion) {
        // 1. Buscar la categoría existente. Si no existe, lanza una excepción.
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada para actualizar"));

        // 2. Actualizar los campos
        categoriaExistente.setNombre(nuevoNombre);
        
        // Si tu entidad Categoria tiene un método setDescripcion:
        // categoriaExistente.setDescripcion(nuevaDescripcion); 

        // 3. Guardar la entidad actualizada (se guarda automáticamente por @Transactional)
        return categoriaRepository.save(categoriaExistente);
    }
    
    // (Solo para ADMIN)
    @Transactional
    public void eliminar(Long id) {
        // Opcional: verificar que no haya productos usando esta categoría antes de borrar
        categoriaRepository.deleteById(id);
    }
}
