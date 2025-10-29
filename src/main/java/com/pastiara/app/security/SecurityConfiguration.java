package com.pastiara.app.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.Customizer; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 
import org.springframework.web.cors.CorsConfiguration; 
import org.springframework.web.cors.CorsConfigurationSource; 
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity // Habilita @PreAuthorize en Controllers (necesario para ADMIN)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyectamos nuestro filtro
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Expone el AuthenticationManager (necesario para el AuthController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	
        	.cors(Customizer.withDefaults())
        	
            .csrf(AbstractHttpConfigurer::disable) 
            
            // La sesión es sin estado (STATELESS) para JWT
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas (login y registro)
                .requestMatchers("/api/auth/**").permitAll() 
                
                // Rutas públicas de consulta (GETs para el frontend)
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                
                // ⭐ CLAVE: Todas las demás rutas de la API requieren un token VÁLIDO (autenticación).
                // Esto permite que el token pase y que el @PreAuthorize("hasRole('ADMIN')")
                // de los Controllers de POST/PUT/DELETE revise el rol.
                .requestMatchers("/api/**").authenticated() 
                
                // Deniega cualquier otra URL no especificada
                .anyRequest().denyAll()
            );

        // Añade nuestro filtro JWT antes del filtro de username/password
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
<<<<<<< HEAD
        // 1. Orígenes Permitidos (¡Importante!)
        // URL de frontend. React/Angular/Vue corre en el puerto 3000:
        configuration.setAllowedOrigins(Arrays.asList("https://pastiara.vercel.app", "http://localhost:5173"));
        
        // 2. Métodos Permitidos
        // Permitimos todos los métodos comunes
=======
        // Configuración de CORS
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:5173"));
>>>>>>> 5c8f15d26698ca424de91e1d948d9095d5a545a7
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}