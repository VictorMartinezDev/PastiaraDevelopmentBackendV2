package com.pastiara.app.service;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.CotizacionRequestDTO;
import com.pastiara.app.dto.CotizacionResponseDTO;
import com.pastiara.app.dto.DetalleCotizacionDTO;
import com.pastiara.app.dto.DetalleCotizacionResponseDTO;
import com.pastiara.app.dto.DireccionEnvioDTO;
import com.pastiara.app.model.Cotizacion;
import com.pastiara.app.model.DetalleCotizacion;
import com.pastiara.app.model.DireccionEnvio;
import com.pastiara.app.model.Producto;
import com.pastiara.app.model.Usuario;
import com.pastiara.app.repository.CotizacionRepository;
import com.pastiara.app.repository.DireccionEnvioRepository;
import com.pastiara.app.repository.ProductoRepository;
import com.pastiara.app.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CotizacionService {

    // (Inyección de todos los repositorios... sin cambios)
    private final CotizacionRepository cotizacionRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;

    public CotizacionService(CotizacionRepository cotizacionRepository,
                             ProductoRepository productoRepository,
                             UsuarioRepository usuarioRepository,
                             DireccionEnvioRepository direccionEnvioRepository) {
        // (Constructor... sin cambios)
        this.cotizacionRepository = cotizacionRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionEnvioRepository = direccionEnvioRepository;
    }
    
    private Usuario getUsuarioLogueado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario logueado no encontrado"));
    }

    /**
     * ¡MÉTODO MODIFICADO!
     * Ahora recibe un solo objeto DTO con toda la información.
     */
    @Transactional
    public CotizacionResponseDTO crearNuevaCotizacion(CotizacionRequestDTO requestDTO) {

        // 1. OBTENER USUARIO (Sin cambios)
        Usuario usuario = getUsuarioLogueado();

        // 2. CREAR Y GUARDAR DIRECCIÓN (Sin cambios)
        DireccionEnvioDTO dirDTO = requestDTO.getDireccion();
        DireccionEnvio nuevaDireccion = new DireccionEnvio();
        nuevaDireccion.setCalle(dirDTO.getCalle()); 
        nuevaDireccion.setColonia(dirDTO.getColonia());
        nuevaDireccion.setMunicipio(dirDTO.getMunicipio());
        nuevaDireccion.setEstado(dirDTO.getEstado());
        nuevaDireccion.setCodigoPostal(dirDTO.getCodigoPostal());
        nuevaDireccion.setUsuario(usuario);
        DireccionEnvio direccionGuardada = direccionEnvioRepository.save(nuevaDireccion);

        // 3. CREAR COTIZACIÓN (Sin cambios)
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setUsuario(usuario);
        cotizacion.setDireccionEnvio(direccionGuardada);
        cotizacion.setTipoDeEvento(requestDTO.getTipoDeEvento());
        cotizacion.setComentarios(requestDTO.getComentarios());

        // 4. LÓGICA DE PRODUCTOS (Sin cambios)
        List<DetalleCotizacion> detallesVerificados = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (DetalleCotizacionDTO detalleDto : requestDTO.getDetalles()) {
        	
        	DetalleCotizacion detalleReal = new DetalleCotizacion();
        	
            Producto producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            
            detalleReal.setProducto(producto);
            detalleReal.setCantidad(detalleDto.getCantidad());
            detalleReal.setPrecioUnitarioCotizado(producto.getPrecio());
            detalleReal.setCotizacion(cotizacion);

            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(detalleReal.getCantidad()));
            
            // 3. Súmalo al total general
            totalCalculado = totalCalculado.add(subtotal);
            
            detallesVerificados.add(detalleReal);
        }

        cotizacion.setTotalCotizado(totalCalculado);
        cotizacion.setDetalles(detallesVerificados); 

        // 5. GUARDAR LA ENTIDAD (Sin cambios)
        Cotizacion cotizacionGuardada = cotizacionRepository.save(cotizacion);

        // 6. --- ¡NUEVO! CONVERTIR A DTO ANTES DE DEVOLVER ---
        // (Esto ocurre dentro de la transacción, por lo que es seguro)
        return convertirEntidadADTO(cotizacionGuardada);
    }
    
    /**
     * Método auxiliar privado para convertir la Entidad a DTO
     */
    private CotizacionResponseDTO convertirEntidadADTO(Cotizacion cotizacion) {
        
        // 1. Convertir la lista de Entidades "DetalleCotizacion"
        // a una lista de DTOs "DetalleCotizacionResponseDTO".
        // Usamos .stream() para iterar y transformar cada elemento.
        List<DetalleCotizacionResponseDTO> detallesDto = cotizacion.getDetalles().stream()
            .map(detalle -> {
                // detalle = un objeto DetalleCotizacion (la entidad)
                DetalleCotizacionResponseDTO dto = new DetalleCotizacionResponseDTO();
                
                // Copiamos los campos del detalle
                dto.setId(detalle.getId());
                dto.setCantidad(detalle.getCantidad());
                dto.setPrecioUnitarioCotizado(detalle.getPrecioUnitarioCotizado());
                
                // Copiamos la información clave del producto asociado
                // Esto es seguro porque estamos dentro del servicio (@Transactional)
                dto.setProductoId(detalle.getProducto().getId());
                dto.setNombreProducto(detalle.getProducto().getNombre());
                
                return dto;
            })
            .collect(Collectors.toList()); // Recolectamos como una Lista

        // 2. Convertir la Entidad "Cotizacion" principal
        CotizacionResponseDTO response = new CotizacionResponseDTO();
        
        // Copiamos los campos de la cotización
        response.setId(cotizacion.getId());
        response.setFechaCreacion(cotizacion.getFechaCreacion());
        response.setTotalCotizado(cotizacion.getTotalCotizado());
        response.setTipoDeEvento(cotizacion.getTipoDeEvento());
        response.setComentarios(cotizacion.getComentarios());
        
        // Copiamos la dirección (esto es seguro, no tiene bucles de vuelta)
        response.setDireccionEnvio(cotizacion.getDireccionEnvio());
        
        // Asignamos la lista de detalles DTO que acabamos de crear
        response.setDetalles(detallesDto);

        return response;
    }
    
    @Transactional(readOnly = true)
    public List<Cotizacion> obtenerMisCotizaciones() {
        Usuario usuario = getUsuarioLogueado();
        return cotizacionRepository.findByUsuarioId(usuario.getId());
    }
}