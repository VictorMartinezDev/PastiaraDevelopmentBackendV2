package com.pastiara.app.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.CategoriaSimpleDTO;
import com.pastiara.app.dto.ProductoResponseDTO;
import com.pastiara.app.model.Producto;
import com.pastiara.app.model.Usuario;
import com.pastiara.app.repository.ProductoRepository;
import com.pastiara.app.repository.UsuarioRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public FavoritoService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    private Usuario getUsuarioLogueado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario logueado no encontrado"));
    }

    private ProductoResponseDTO convertirProductoADTO(Producto producto) {
        CategoriaSimpleDTO catDto = new CategoriaSimpleDTO();
        catDto.setId(producto.getCategoria().getId());
        catDto.setNombre(producto.getCategoria().getNombre());
        
        ProductoResponseDTO responseDto = new ProductoResponseDTO();
        responseDto.setId(producto.getId());
        responseDto.setNombre(producto.getNombre());
        responseDto.setDescripcion(producto.getDescripcion());
        responseDto.setPrecio(producto.getPrecio());
        responseDto.setImagenUrl(producto.getImagenUrl());
        responseDto.setCategoria(catDto);
        
        return responseDto;
    }

    @Transactional(readOnly = true)
    public Set<ProductoResponseDTO> obtenerMisFavoritos() {
        Usuario usuario = getUsuarioLogueado();
        
        // Obtenemos las entidades
        Set<Producto> favoritos = usuario.getProductosFavoritos();
        
        // Las convertimos a DTOs
        return favoritos.stream()
            .map(this::convertirProductoADTO)
            .collect(Collectors.toSet());
    }

    @Transactional
    public void agregarAFavoritos(Long productoId) {
        Usuario usuario = getUsuarioLogueado();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        usuario.getProductosFavoritos().add(producto);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void quitarDeFavoritos(Long productoId) {
        Usuario usuario = getUsuarioLogueado();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        usuario.getProductosFavoritos().remove(producto);
        usuarioRepository.save(usuario);
    }
}