package com.pastiara.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pastiara.app.service.UsuarioService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioService usuarioService; // Tu UserDetailsService

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Obtener el token del request
            String jwt = getJwtFromRequest(request);

            // 2. Validar el token
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. Obtener el usuario del token
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 4. Cargar los detalles del usuario
                UserDetails userDetails = usuarioService.loadUserByUsername(username);
                
                // 5. Crear la autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Establecer la autenticación en el contexto de seguridad
                // ¡Esto es lo que permite que @PreAuthorize funcione!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario", ex);
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extraer el token "Bearer" de la cabecera
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Devuelve solo el token, sin "Bearer "
        }
        return null;
    }
}