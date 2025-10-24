package com.pastiara.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.model.Usuario;
import com.pastiara.app.repository.UsuarioRepository;

import java.util.Collection;
import java.util.Collections;

@Service // ¡Es un servicio!
public class UsuarioService implements UserDetailsService { // <-- ¡Importante!

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // <-- Vuelve a ser 'final'

    // ¡Vuelve al constructor normal!
    public UsuarioService(UsuarioRepository usuarioRepository, 
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Este método es el que usa Spring Security para el login.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Creamos la "autoridad" (el rol)
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()) // Ej: "ROLE_CLIENTE"
        );

        // Devolvemos el objeto "User" que Spring Security entiende
        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    /**
     * Método para registrar un nuevo usuario (para tu controlador de registro).
     */
    @Transactional
    public Usuario registrarNuevoUsuario(String nombre, String email, String password, String numeroTelefono) { // <-- AÑADIR
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setNumeroTelefono(numeroTelefono); 
        
        return usuarioRepository.save(nuevoUsuario);
    }

}