package com.pastiara.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastiara.app.model.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Método de consulta para buscar todos los productos de una categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // Método para buscar productos por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
