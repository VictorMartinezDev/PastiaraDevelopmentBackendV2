package com.pastiara.app.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.model.Producto;
import com.pastiara.app.model.Usuario;
import com.pastiara.app.repository.ProductoRepository;
import com.pastiara.app.repository.UsuarioRepository;

import java.util.Set;

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

    @Transactional(readOnly = true)
    public Set<Producto> obtenerMisFavoritos() {
        Usuario usuario = getUsuarioLogueado();
        // Forzamos la carga de la colección 'lazy' dentro de la transacción
        return Set.copyOf(usuario.getProductosFavoritos()); 
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