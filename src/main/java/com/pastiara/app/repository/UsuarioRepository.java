package com.pastiara.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastiara.app.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    //Buscamos a usuario por su email
    Optional<Usuario> findByEmail(String email);
}
