package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.CompraDto;
import com.back.retro_vlt.dto.PagoDto;
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
    public Orden procesarCompra(CompraDto dto) {
        if (dto == null) {
            return null;
        }

        Orden orden = new Orden();
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("PENDIENTE_PAGO");

        String fechaCodigo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int aleatorio = 1000 + new Random().nextInt(9000);
        orden.setNumeroOrden("#" + fechaCodigo + "-" + aleatorio);

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

        if (dto.getMetodoPago() != null && !dto.getMetodoPago().isBlank()) {
            orden.setMetodoPago(dto.getMetodoPago());
        }

        Double total = 0.0;
        List<DetalleOrden> detalles = new ArrayList<>();

        if (dto.getItems() != null) {
            for (CompraDto.ItemCompraDto itemDto : dto.getItems()) {
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
    public Orden crearCheckout(CompraDto dto) {
        return procesarCompra(dto);
    }

    @Transactional
    public Orden procesarPago(Long ordenId, PagoDto dto) {
        Orden orden = ordenRepository.findById(ordenId).orElse(null);
        if (orden == null) {
            return null;
        }

        if (dto != null && Boolean.TRUE.equals(dto.getSimularError())) {
            orden.setEstado("FALLIDO");
            if (dto.getMetodoPago() != null) {
                orden.setMetodoPago(dto.getMetodoPago());
            }
            return ordenRepository.save(orden);
        }

        if (dto != null && dto.getMetodoPago() != null && !dto.getMetodoPago().isBlank()) {
            orden.setMetodoPago(dto.getMetodoPago());
        }

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
        if (numeroOrden == null) {
            return null;
        }
        String limpio = numeroOrden.trim();
        Orden o = ordenRepository.findByNumeroOrden(limpio);
        if (o == null) {
            if (limpio.startsWith("#")) {
                o = ordenRepository.findByNumeroOrden(limpio.substring(1));
            } else {
                o = ordenRepository.findByNumeroOrden("#" + limpio);
            }
        }
        return o;
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

    @Transactional
    public boolean eliminar(Long id) {
        Orden o = obtenerPorId(id);
        if (o != null) {
            ordenRepository.delete(o);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean eliminarPorNumero(String numeroOrden) {
        Orden o = obtenerPorNumeroOrden(numeroOrden);
        if (o != null) {
            ordenRepository.delete(o);
            return true;
        }
        return false;
    }

    @Transactional
    public Orden actualizar(Long id, Orden datosNuevos) {
        Orden orden = obtenerPorId(id);
        if (orden == null) {
            return null;
        }
        if (datosNuevos.getEstado() != null) {
            orden.setEstado(datosNuevos.getEstado());
        }
        if (datosNuevos.getMetodoPago() != null) {
            orden.setMetodoPago(datosNuevos.getMetodoPago());
        }
        if (datosNuevos.getTotal() != null) {
            orden.setTotal(datosNuevos.getTotal());
        }
        if (datosNuevos.getCalle() != null) {
            orden.setCalle(datosNuevos.getCalle());
        }
        if (datosNuevos.getDepartamento() != null) {
            orden.setDepartamento(datosNuevos.getDepartamento());
        }
        if (datosNuevos.getRegion() != null) {
            orden.setRegion(datosNuevos.getRegion());
        }
        if (datosNuevos.getComuna() != null) {
            orden.setComuna(datosNuevos.getComuna());
        }
        if (datosNuevos.getIndicaciones() != null) {
            orden.setIndicaciones(datosNuevos.getIndicaciones());
        }
        return ordenRepository.save(orden);
    }
}
