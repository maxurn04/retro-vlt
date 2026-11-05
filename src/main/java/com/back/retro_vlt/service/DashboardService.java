package com.back.retro_vlt.service;

import com.back.retro_vlt.dto.MetricasDashboardDto;
import com.back.retro_vlt.model.Producto;
import com.back.retro_vlt.repository.OrdenRepository;
import com.back.retro_vlt.repository.ProductoRepository;
import com.back.retro_vlt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public MetricasDashboardDto obtenerMetricas() {
        Double totalCompras = (double) ordenRepository.count();
        Double totalIngresos = ordenRepository.sumTotalIngresos();
        Double usuarios = (double) usuarioRepository.count();

        List<Producto> productos = productoRepository.findAll();
        Double cantProductos = (double) productos.size();

        double totalStock = 0.0;
        for (Producto p : productos) {
            if (p.getStock() != null) {
                totalStock += p.getStock();
            }
        }

        Double criticos = (double) productoRepository.findProductosCriticos().size();

        MetricasDashboardDto dto = new MetricasDashboardDto();
        dto.setTotalCompras(totalCompras);
        dto.setIngresosTotales(totalIngresos != null ? totalIngresos : 0.0);
        dto.setProductosInventario(cantProductos);
        dto.setStockTotalUnidades(totalStock);
        dto.setUsuariosRegistrados(usuarios);
        dto.setProductosCriticosAlerta(criticos);

        return dto;
    }
}
