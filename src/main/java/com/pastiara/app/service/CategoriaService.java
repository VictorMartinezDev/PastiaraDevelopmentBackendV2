package com.pastiara.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.model.Categoria;
import com.pastiara.app.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // (Solo para ADMIN)
    @Transactional
    public Categoria crear(String nombre, String descripcion) {
        Categoria nuevaCategoria = new Categoria(nombre);
        return categoriaRepository.save(nuevaCategoria);
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
