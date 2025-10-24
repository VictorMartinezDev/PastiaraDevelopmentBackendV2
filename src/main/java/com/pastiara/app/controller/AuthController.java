package com.pastiara.app.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pastiara.app.security.*;
import org.springframework.security.authentication.AuthenticationManager; // <-- Importar
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // <-- Importar
import org.springframework.security.core.Authentication; // <-- Importar
import org.springframework.security.core.context.SecurityContextHolder; // <-- Importar
import com.pastiara.app.model.Usuario;
import org.springframework.security.core.GrantedAuthority; // <-- ¡IMPORTA ESTE!
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <-- ¡IMPORTA ESTE!
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pastiara.app.dto.AuthResponseDTO;
import com.pastiara.app.dto.LoginDTO;
import com.pastiara.app.dto.PerfilUsuarioDTO;
import com.pastiara.app.dto.RegistroDTO;
import com.pastiara.app.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager; // <-- Inyectar
    private final JwtTokenProvider tokenProvider; // <-- Inyectar

    public AuthController(UsuarioService usuarioService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
this.usuarioService = usuarioService;
this.authenticationManager = authenticationManager;
this.tokenProvider = tokenProvider;
}

    @PostMapping("/register")
    // Cambiamos el tipo de respuesta a 'AuthResponseDTO'
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO) {
        try {
            // 1. Registra al usuario (tu servicio debe devolver el 'Usuario' creado)
            Usuario nuevoUsuario = usuarioService.registrarNuevoUsuario(
                registroDTO.getNombre(),
                registroDTO.getEmail(),
                registroDTO.getPassword(),
                registroDTO.getNumeroTelefono()
            );

            // 2. --- ¡NUEVA LÓGICA DE AUTO-LOGIN! ---
            // Creamos manualmente el objeto de autenticación para el nuevo usuario.
            
            // 2a. Obtenemos sus roles (sabemos que es CLIENTE)
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + nuevoUsuario.getRol().name())
            );

            // 2b. Creamos el token de autenticación
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    nuevoUsuario.getEmail(), // El "principal"
                    null, // La contraseña (no la necesitamos, ya está autenticado)
                    authorities // Los roles
            );

            // 3. Generamos el token JWT
            String token = tokenProvider.generateToken(authentication);

            // 4. Devolvemos el token (igual que el login)
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(token));

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @GetMapping("/profile")
    // ¡Ahora devuelve el DTO!
    public ResponseEntity<PerfilUsuarioDTO> obtenerPerfilUsuario(Authentication authentication) {
        if (authentication == null) {
            // No es necesario, Spring Security lo bloqueará, pero es una doble verificación.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email); 

            // Convertimos la Entidad a DTO
            PerfilUsuarioDTO perfil = new PerfilUsuarioDTO();
            perfil.setId(usuario.getId());
            perfil.setNombre(usuario.getNombre());
            perfil.setEmail(usuario.getEmail());
            perfil.setNumeroTelefono(usuario.getNumeroTelefono());
            perfil.setRol(usuario.getRol());

            return ResponseEntity.ok(perfil);
            
        } catch (UsernameNotFoundException e) {
            // Este caso es raro si el token es válido, pero es bueno tenerlo.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> autenticarUsuario(@RequestBody LoginDTO loginDTO) {
        // ... (Este método no cambia)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}