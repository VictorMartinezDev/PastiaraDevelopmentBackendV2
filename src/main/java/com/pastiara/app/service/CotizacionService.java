package com.pastiara.app.service;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastiara.app.dto.CotizacionRequestDTO;
import com.pastiara.app.dto.CotizacionResponseDTO;
import com.pastiara.app.dto.DetalleCotizacionDTO;
import com.pastiara.app.dto.DetalleCotizacionResponseDTO;
import com.pastiara.app.dto.DireccionEnvioDTO;
import com.pastiara.app.dto.DireccionEnvioResponseDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        
     // Convertimos el String "YYYY-MM-DD" a un objeto LocalDate
        cotizacion.setFechaEvento(LocalDate.parse(requestDTO.getFechaEvento()));

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
                dto.setId(detalle.getId());
                dto.setCantidad(detalle.getCantidad());
                dto.setPrecioUnitarioCotizado(detalle.getPrecioUnitarioCotizado());
                dto.setProductoId(detalle.getProducto().getId());
                dto.setNombreProducto(detalle.getProducto().getNombre());
                dto.setCategoriaNombre(detalle.getProducto().getCategoria().getNombre());
                
                return dto;
            })
            .collect(Collectors.toList()); // Recolectamos como una Lista
        
        DireccionEnvioResponseDTO direccionDto = new DireccionEnvioResponseDTO();
        DireccionEnvio direccionEntidad = cotizacion.getDireccionEnvio(); // Accede al proxy
        
        direccionDto.setId(direccionEntidad.getId());
        direccionDto.setCalle(direccionEntidad.getCalle());
        direccionDto.setColonia(direccionEntidad.getColonia());
        direccionDto.setMunicipio(direccionEntidad.getMunicipio());
        direccionDto.setEstado(direccionEntidad.getEstado());
        direccionDto.setCodigoPostal(direccionEntidad.getCodigoPostal());

        // 2. Convertir la Entidad "Cotizacion" principal
        CotizacionResponseDTO response = new CotizacionResponseDTO();
        
        // Copiamos los campos de la cotización
        response.setId(cotizacion.getId());
        response.setFechaCreacion(cotizacion.getFechaCreacion());
        response.setTotalCotizado(cotizacion.getTotalCotizado());
        response.setTipoDeEvento(cotizacion.getTipoDeEvento());
        response.setComentarios(cotizacion.getComentarios());
        
        // Copiamos la dirección (esto es seguro, no tiene bucles de vuelta)
        response.setDireccionEnvio(direccionDto);
        
        // Asignamos la lista de detalles DTO que acabamos de crear
        response.setDetalles(detallesDto);
        
        response.setFechaEvento(cotizacion.getFechaEvento());

        return response;
    }
    
    @Transactional(readOnly = true)
    public List<CotizacionResponseDTO> obtenerMisCotizaciones() {
        Usuario usuario = getUsuarioLogueado();
        List<Cotizacion> cotizaciones = cotizacionRepository.findByUsuarioId(usuario.getId());
        
        return cotizaciones.stream()
                .map(this::convertirEntidadADTO) // Reutiliza el método auxiliar
                .collect(Collectors.toList());
    }
}