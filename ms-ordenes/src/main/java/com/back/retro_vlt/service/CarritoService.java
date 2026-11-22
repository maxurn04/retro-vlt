package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.CarritoItemDto;
import com.back.retro_vlt.dto.CarritoItemRequestDto;
import com.back.retro_vlt.model.CarritoItem;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.CarritoItemRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<CarritoItemDto> obtenerItemsPorUsuario(Long usuarioId) {
        List<CarritoItem> items = carritoItemRepository.findByUsuarioId(usuarioId);
        List<CarritoItemDto> respuesta = new ArrayList<>();
        for (CarritoItem item : items) {
            respuesta.add(mapearADto(item));
        }
        return respuesta;
    }

    @Transactional
    public CarritoItemDto guardarOSincronizarItem(Long usuarioId, CarritoItemRequestDto request) {
        if (usuarioId == null || request == null || request.getProductoId() == null) {
            return null;
        }

        Long productoId = request.getProductoId();
        int cantidad = (request.getCantidad() != null && request.getCantidad() > 0) ? request.getCantidad() : 1;

        Optional<CarritoItem> existenteOpt = carritoItemRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);
        CarritoItem itemAGuardar;

        if (existenteOpt.isPresent()) {
            itemAGuardar = existenteOpt.get();
            if (Boolean.TRUE.equals(request.getSobrescribir())) {
                itemAGuardar.setCantidad(cantidad);
            } else if (request.getModificador() != null) {
                int nueva = itemAGuardar.getCantidad() + request.getModificador();
                if (nueva <= 0) {
                    carritoItemRepository.delete(itemAGuardar);
                    return null;
                }
                itemAGuardar.setCantidad(nueva);
            } else {
                itemAGuardar.setCantidad(itemAGuardar.getCantidad() + cantidad);
            }
        } else {
            itemAGuardar = CarritoItem.builder()
                    .usuarioId(usuarioId)
                    .productoId(productoId)
                    .cantidad(cantidad)
                    .build();
        }

        CarritoItem guardado = carritoItemRepository.save(itemAGuardar);
        return mapearADto(guardado);
    }

    @Transactional
    public CarritoItemDto actualizarCantidad(Long usuarioId, Long productoId, Integer nuevaCantidad, Integer modificador) {
        if (usuarioId == null || productoId == null) {
            return null;
        }

        Optional<CarritoItem> existenteOpt = carritoItemRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);

        if (modificador != null) {
            if (existenteOpt.isPresent()) {
                CarritoItem item = existenteOpt.get();
                int nueva = item.getCantidad() + modificador;
                if (nueva <= 0) {
                    carritoItemRepository.delete(item);
                    return null;
                }
                item.setCantidad(nueva);
                return mapearADto(carritoItemRepository.save(item));
            } else if (modificador > 0) {
                CarritoItem nuevo = CarritoItem.builder()
                        .usuarioId(usuarioId)
                        .productoId(productoId)
                        .cantidad(modificador)
                        .build();
                return mapearADto(carritoItemRepository.save(nuevo));
            } else {
                return null;
            }
        }

        if (nuevaCantidad != null) {
            if (nuevaCantidad <= 0) {
                existenteOpt.ifPresent(carritoItem -> carritoItemRepository.delete(carritoItem));
                return null;
            }

            CarritoItem item = existenteOpt.orElseGet(() -> CarritoItem.builder()
                    .usuarioId(usuarioId)
                    .productoId(productoId)
                    .build());
            item.setCantidad(nuevaCantidad);
            return mapearADto(carritoItemRepository.save(item));
        }

        return existenteOpt.map(this::mapearADto).orElse(null);
    }

    @Transactional
    public boolean eliminarItem(Long usuarioId, Long productoId) {
        if (usuarioId == null || productoId == null) {
            return false;
        }
        Optional<CarritoItem> existente = carritoItemRepository.findByUsuarioIdAndProductoId(usuarioId, productoId);
        if (existente.isPresent()) {
            carritoItemRepository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean vaciarCarrito(Long usuarioId) {
        if (usuarioId == null) {
            return false;
        }
        carritoItemRepository.deleteByUsuarioId(usuarioId);
        return true;
    }

    private CarritoItemDto mapearADto(CarritoItem item) {
        Producto prod = item.getProducto();
        if (prod == null && item.getProductoId() != null) {
            prod = productoRepository.findById(item.getProductoId()).orElse(null);
        }

        Double precio = 0.0;
        String nombre = "Producto #" + item.getProductoId();
        String imagenUrl = "";

        if (prod != null) {
            nombre = prod.getNombre();
            imagenUrl = (prod.getImagenUrl() != null) ? prod.getImagenUrl() : "";
            if (Boolean.TRUE.equals(prod.getEsOferta()) && prod.getPrecioOferta() != null) {
                precio = prod.getPrecioOferta();
            } else if (prod.getPrecio() != null) {
                precio = prod.getPrecio();
            }
        }

        int cantidad = (item.getCantidad() != null) ? item.getCantidad() : 1;
        Double subtotal = precio * cantidad;

        return CarritoItemDto.builder()
                .id(item.getId())
                .productoId(item.getProductoId())
                .nombre(nombre)
                .precio(precio)
                .cantidad(cantidad)
                .imagenUrl(imagenUrl)
                .img(imagenUrl)
                .subtotal(subtotal)
                .build();
    }
}
