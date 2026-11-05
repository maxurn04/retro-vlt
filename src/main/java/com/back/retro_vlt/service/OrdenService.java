package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.CheckoutRequestDto;
import com.back.retro_vlt.dto.PagarRequestDto;
import com.back.retro_vlt.model.DetalleOrden;
import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.repository.OrdenRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Orden crearCheckout(CheckoutRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Orden orden = new Orden();
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("PENDIENTE_PAGO");

        // Generar número de orden único (Ej: #20240705 o #YYYYMMDD-XXXX)
        String fechaCodigo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int aleatorio = 1000 + new Random().nextInt(9000);
        orden.setNumeroOrden("#" + fechaCodigo + "-" + aleatorio);

        // Cliente y Dirección
        if (dto.getCliente() != null && dto.getCliente().getUsuarioId() != null) {
            Usuario user = usuarioRepository.findById(dto.getCliente().getUsuarioId()).orElse(null);
            orden.setUsuario(user);
        }

        if (dto.getDireccion() != null) {
            orden.setCalle(dto.getDireccion().getCalle());
            orden.setDepartamento(dto.getDireccion().getDepartamento());
            orden.setRegion(dto.getDireccion().getRegion());
            orden.setComuna(dto.getDireccion().getComuna());
            orden.setIndicaciones(dto.getDireccion().getIndicaciones());
        }

        // Ítems y cálculo del total
        Double total = 0.0;
        List<DetalleOrden> detalles = new ArrayList<>();

        if (dto.getItems() != null) {
            for (CheckoutRequestDto.ItemCheckoutDto itemDto : dto.getItems()) {
                if (itemDto == null || itemDto.getProductoId() == null) {
                    continue;
                }
                Producto prod = productoRepository.findById(itemDto.getProductoId()).orElse(null);
                if (prod == null) {
                    continue;
                }

                Double precioUnitario = (Boolean.TRUE.equals(prod.getEsOferta()) && prod.getPrecioOferta() != null)
                        ? prod.getPrecioOferta()
                        : prod.getPrecio();

                int cantidad = itemDto.getCantidad() != null ? itemDto.getCantidad() : 1;
                Double subtotal = precioUnitario * cantidad;
                total += subtotal;

                DetalleOrden detalle = new DetalleOrden();
                detalle.setOrden(orden);
                detalle.setProducto(prod);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setSubtotal(subtotal);

                detalles.add(detalle);
            }
        }

        orden.setTotal(total);
        orden.setDetalles(detalles);

        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden procesarPago(Long ordenId, PagarRequestDto dto) {
        Orden orden = ordenRepository.findById(ordenId).orElse(null);
        if (orden == null) {
            return null;
        }

        if (dto != null && Boolean.TRUE.equals(dto.getSimularError())) {
            orden.setEstado("FALLIDO");
            return ordenRepository.save(orden);
        }

        // Si el pago es exitoso, descontar stock de productos
        if (orden.getDetalles() != null) {
            for (DetalleOrden det : orden.getDetalles()) {
                Producto p = det.getProducto();
                if (p != null && p.getStock() != null) {
                    int nuevoStock = Math.max(0, p.getStock() - det.getCantidad());
                    p.setStock(nuevoStock);
                    productoRepository.save(p);
                }
            }
        }

        orden.setEstado("PAGADO");
        return ordenRepository.save(orden);
    }

    public Orden obtenerPorNumeroOrden(String numeroOrden) {
        return ordenRepository.findByNumeroOrden(numeroOrden);
    }

    public Orden obtenerPorId(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    public List<Orden> obtenerTodas() {
        return ordenRepository.findAllByOrderByFechaDesc();
    }

    public List<Orden> obtenerPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }
}
