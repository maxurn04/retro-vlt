package com.back.retro_vlt.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa una orden o boleta de compra")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único autoincremental de la orden", example = "1")
    private Long id;

    @Column(name = "numero_orden", nullable = false, unique = true, length = 30)
    @Schema(description = "Número o código alfanumérico visible de la boleta", example = "#20240705")
    private String numeroOrden;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de creación de la orden", example = "2026-11-05T14:30:00")
    private LocalDateTime fecha;

    @Column(nullable = false)
    @Schema(description = "Monto total de la compra en CLP", example = "28775.0")
    private Double total;

    @Column(nullable = false, length = 20)
    @Schema(description = "Estado actual del flujo de la orden", example = "PAGADO", allowableValues = {"PENDIENTE_PAGO", "PAGADO", "FALLIDO", "EN_PREPARACION", "DESPACHADO", "ENTREGADO", "CANCELADO"})
    private String estado = "PENDIENTE_PAGO";

    @Column(name = "metodo_pago", length = 50)
    @Schema(description = "Método o pasarela utilizada para el pago", example = "Tarjeta Débito Webpay")
    private String metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    @Schema(description = "Usuario cliente que realizó la compra")
    private Usuario usuario;

    @Column(length = 150)
    @Schema(description = "Calle y numeración de despacho", example = "Los Crisantemos, Edificio Norte")
    private String calle;

    @Column(length = 50)
    @Schema(description = "Departamento, oficina o block", example = "Depto 603")
    private String departamento;

    @Column(length = 80)
    @Schema(description = "Región de entrega", example = "Región Metropolitana de Santiago")
    private String region;

    @Column(length = 80)
    @Schema(description = "Comuna de entrega", example = "Cerrillos")
    private String comuna;

    @Column(length = 500)
    @Schema(description = "Indicaciones especiales o notas para el repartidor", example = "El martes no estaremos en el depto, pero puede dejárselo con el conserje.")
    private String indicaciones;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "Lista de ítems y productos contenidos en la orden")
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
