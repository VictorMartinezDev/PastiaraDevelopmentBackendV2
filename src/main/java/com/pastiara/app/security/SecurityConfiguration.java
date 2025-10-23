package com.pastiara.app.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.Customizer; // CORDS
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; 
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 
import org.springframework.web.cors.CorsConfiguration; // CORDS
import org.springframework.web.cors.CorsConfigurationSource; // CORDS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;// CORDS

@Configuration
@EnableMethodSecurity // Habilita @PreAuthorize
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyectamos nuestro filtro
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // ¡NUEVO! Expone el AuthenticationManager como un Bean
    // Lo usaremos en el AuthController para el login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	
        	.cors(Customizer.withDefaults()) //CORDS
        	
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF
            
            // ¡NUEVO! Le dice a Spring que la sesión será sin estado (STATELESS)
            // Esto es fundamental para JWT
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas (login y registro)
                .requestMatchers("/api/auth/**").permitAll() 
                // Rutas públicas de consulta (ver productos/categorías)
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                // Todas las demás requieren autenticación
                .anyRequest().authenticated() 
            );

        // ¡NUEVO! Añade nuestro filtro JWT antes del filtro de username/password
        // Así validamos el token antes que nada.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 1. Orígenes Permitidos (¡Importante!)
        // URL de frontend. React/Angular/Vue corre en el puerto 3000:
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:5173"));
        
        // 2. Métodos Permitidos
        // Permitimos todos los métodos comunes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // 3. Cabeceras Permitidas
        // Permitimos cabeceras comunes y las de autenticación
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        
        // 4. Permitir Credenciales
        // Esto es necesario para que el frontend pueda enviar el token JWT
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // 5. Aplicar esta configuración a TODAS las rutas de tu API
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}