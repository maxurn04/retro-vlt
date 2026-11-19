package com.back.retro_vlt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "ordenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", nullable = false, unique = true, length = 30)
    private String numeroOrden;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE_PAGO";

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 150)
    private String calle;

    @Column(length = 50)
    private String departamento;

    @Column(length = 80)
    private String region;

    @Column(length = 80)
    private String comuna;

    @Column(length = 500)
    private String indicaciones;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleOrden> detalles = new ArrayList<>();

    public String getNumero() {
        return numeroOrden;
    }

    public void setNumero(String numero) {
        this.numeroOrden = numero;
    }

    public Map<String, Object> getCliente() {
        Map<String, Object> cli = new HashMap<>();
        if (usuario != null) {
            cli.put("nombre", usuario.getNombre());
            cli.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
            cli.put("correo", usuario.getCorreo());
            cli.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
            cli.put("run", usuario.getRun() != null ? usuario.getRun() : "");
            cli.put("calle", calle != null ? calle : "");
            cli.put("departamento", departamento != null ? departamento : "");
            cli.put("region", region != null ? region : "");
            cli.put("comuna", comuna != null ? comuna : "");
            cli.put("indicaciones", indicaciones != null ? indicaciones : "");
        } else {
            cli.put("nombre", "Cliente");
            cli.put("apellidos", "General");
            cli.put("correo", "");
            cli.put("telefono", "");
            cli.put("run", "");
            cli.put("calle", calle != null ? calle : "");
            cli.put("departamento", departamento != null ? departamento : "");
            cli.put("region", region != null ? region : "");
            cli.put("comuna", comuna != null ? comuna : "");
            cli.put("indicaciones", indicaciones != null ? indicaciones : "");
        }
        return cli;
    }

    public List<Map<String, Object>> getProductos() {
        List<Map<String, Object>> list = new ArrayList<>();
        if (detalles != null) {
            for (DetalleOrden d : detalles) {
                Map<String, Object> item = new HashMap<>();
                if (d.getProducto() != null) {
                    item.put("id", d.getProducto().getId());
                    item.put("codigo", d.getProducto().getCodigo());
                    item.put("nombre", d.getProducto().getNombre());
                    item.put("precio", d.getPrecioUnitario());
                    item.put("img", d.getProducto().getImagenUrl());
                }
                item.put("cantidad", d.getCantidad());
                item.put("subtotal", d.getSubtotal());
                list.add(item);
            }
        }
        return list;
    }
}
