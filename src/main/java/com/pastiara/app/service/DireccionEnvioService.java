package com.pastiara.app.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.DireccionEnvioDTO;
import com.pastiara.app.model.DireccionEnvio;
import com.pastiara.app.model.Usuario;
import com.pastiara.app.repository.DireccionEnvioRepository;
import com.pastiara.app.repository.UsuarioRepository;

import java.util.List;

@Service
public class DireccionEnvioService {

    private final DireccionEnvioRepository direccionEnvioRepository;
    private final UsuarioRepository usuarioRepository;

    public DireccionEnvioService(DireccionEnvioRepository direccionEnvioRepository, UsuarioRepository usuarioRepository) {
        this.direccionEnvioRepository = direccionEnvioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene el usuario actualmente logueado desde el contexto de seguridad.
     */
    private Usuario getUsuarioLogueado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario logueado no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<DireccionEnvio> obtenerMisDirecciones() {
        Usuario usuario = getUsuarioLogueado();
        return direccionEnvioRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public DireccionEnvio crearNuevaDireccion(DireccionEnvio direccion) {
        Usuario usuario = getUsuarioLogueado();
        direccion.setUsuario(usuario); // Vinculamos la dirección al usuario
        return direccionEnvioRepository.save(direccion);
    }
    @Transactional
    public DireccionEnvio actualizarDireccion(Long direccionId, DireccionEnvioDTO dto) {
        // 1. Obtener el usuario logueado (para seguridad)
        Usuario usuario = getUsuarioLogueado();
        
        // 2. Buscar la dirección existente.
        DireccionEnvio direccionExistente = direccionEnvioRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // 3. ¡Seguridad! Asegurarnos que solo modifique sus propias direcciones
        if (!direccionExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acceso denegado: no puedes modificar esta dirección");
        }

        // 4. Aplicar las modificaciones del DTO a la entidad
        direccionExistente.setCalle(dto.getCalle());
        direccionExistente.setColonia(dto.getColonia());
        direccionExistente.setMunicipio(dto.getMunicipio());
        direccionExistente.setEstado(dto.getEstado());
        direccionExistente.setCodigoPostal(dto.getCodigoPostal());

        // 5. Guardar los cambios
        return direccionEnvioRepository.save(direccionExistente);
    }
    @Transactional
    public void eliminarDireccion(Long direccionId) {
        Usuario usuario = getUsuarioLogueado();
        DireccionEnvio direccion = direccionEnvioRepository.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // ¡Seguridad! Asegurarnos que solo borre sus propias direcciones
        if (!direccion.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acceso denegado: no puedes borrar esta dirección");
        }
        
        // Opcional: verificar que la dirección no esté en uso por una cotización activa
        
        direccionEnvioRepository.delete(direccion);
    }
}