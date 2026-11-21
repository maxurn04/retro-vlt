package com.back.retro_vlt.controller.admin;

import com.back.retro_vlt.model.Orden;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.service.OrdenService;
import com.back.retro_vlt.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@Tag(name = "Admin - Usuarios (CRUD & Historial)", description = "CRUD de usuarios, roles de acceso e historial de compras para administradores")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna el listado completo de usuarios registrados en el sistema, incluyendo sus roles (ADMIN o CLIENTE).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Usuario.class)),
                            examples = @ExampleObject(name = "Lista de Usuarios", value = "[\n  {\n    \"id\": 1,\n    \"run\": \"12.345.678-9\",\n    \"nombre\": \"Administrador\",\n    \"apellidos\": \"RetroVLT\",\n    \"correo\": \"admin@tienda.cl\",\n    \"rol\": \"ADMIN\",\n    \"telefono\": \"+56911223344\",\n    \"direccionDefecto\": \"Av. Providencia 1234\"\n  },\n  {\n    \"id\": 2,\n    \"run\": \"19.876.543-2\",\n    \"nombre\": \"Pedro\",\n    \"apellidos\": \"Hacker\",\n    \"correo\": \"pedro.hacker20@example.com\",\n    \"rol\": \"CLIENTE\",\n    \"telefono\": \"+56999887766\",\n    \"direccionDefecto\": \"Los Crisantemos, Edificio Norte, Depto 603, Cerrillos\"\n  }\n]")))
    })
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario (CREATE)", description = "Registra un nuevo usuario en la base de datos con rol asignado (CLIENTE o ADMIN). Valida unicidad de correo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(name = "Usuario Creado", value = "{\n  \"id\": 3,\n  \"run\": \"18.765.432-1\",\n  \"nombre\": \"Valentina\",\n  \"apellidos\": \"Morales\",\n  \"correo\": \"valentina.morales@retrovlt.cl\",\n  \"rol\": \"ADMIN\",\n  \"telefono\": \"+56987654321\",\n  \"fechaNacimiento\": \"1995-04-12\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Providencia\",\n  \"direccionDefecto\": \"Av. Providencia 1234, Oficina 402\"\n}"))),
            @ApiResponse(responseCode = "400", description = "No se pudo registrar el usuario por correo duplicado o datos inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Error Registro", value = "{\n  \"error\": \"No se pudo registrar el usuario. El correo ya existe o los datos son inválidos.\"\n}")))
    })
    public ResponseEntity<?> crear(
            @RequestBody(description = "Datos completos del nuevo usuario (listo para Try it out)", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(name = "Ejemplo Nuevo Usuario", value = "{\n  \"run\": \"18.765.432-1\",\n  \"nombre\": \"Valentina\",\n  \"apellidos\": \"Morales\",\n  \"correo\": \"valentina.morales@retrovlt.cl\",\n  \"password\": \"AdminPass2026!\",\n  \"rol\": \"ADMIN\",\n  \"telefono\": \"+56987654321\",\n  \"fechaNacimiento\": \"1995-04-12\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Providencia\",\n  \"direccionDefecto\": \"Av. Providencia 1234, Oficina 402\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        if (nuevo == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar el usuario. El correo ya existe o los datos son inválidos."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mostrar detalle de usuario", description = "Retorna el perfil completo y datos de contacto de un usuario específico dado su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(name = "Detalle Usuario", value = "{\n  \"id\": 2,\n  \"run\": \"19.876.543-2\",\n  \"nombre\": \"Pedro\",\n  \"apellidos\": \"Hacker\",\n  \"correo\": \"pedro.hacker20@example.com\",\n  \"rol\": \"CLIENTE\",\n  \"telefono\": \"+56999887766\",\n  \"fechaNacimiento\": \"1998-08-20\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"direccionDefecto\": \"Los Crisantemos, Edificio Norte, Depto 603, Cerrillos\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "2", required = true)
            @PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar usuario (UPDATE)", description = "Actualiza información personal, dirección de despacho predeterminada, teléfono o rol del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(name = "Usuario Modificado", value = "{\n  \"id\": 2,\n  \"run\": \"19.876.543-2\",\n  \"nombre\": \"Pedro\",\n  \"apellidos\": \"Hacker Valenzuela\",\n  \"correo\": \"pedro.hacker20@example.com\",\n  \"rol\": \"CLIENTE\",\n  \"telefono\": \"+56999887766\",\n  \"fechaNacimiento\": \"1998-08-20\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"direccionDefecto\": \"Los Crisantemos 120, Torre B, Depto 603\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del usuario a modificar", example = "2", required = true)
            @PathVariable Long id,
            @RequestBody(description = "Datos modificados del usuario", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(name = "Ejemplo Editar Usuario", value = "{\n  \"nombre\": \"Pedro\",\n  \"apellidos\": \"Hacker Valenzuela\",\n  \"telefono\": \"+56999887766\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Cerrillos\",\n  \"direccionDefecto\": \"Los Crisantemos 120, Torre B, Depto 603\"\n}")))
            @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (DELETE)", description = "Elimina permanentemente a un usuario del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado con éxito",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Respuesta Eliminación", value = "{\n  \"exito\": true,\n  \"mensaje\": \"Usuario eliminado con éxito\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del usuario a eliminar", example = "2", required = true)
            @PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Usuario eliminado con éxito"));
    }

    @GetMapping(value = {"/{id}/historial-compras", "/{id}/compras"})
    @Operation(summary = "Obtener historial de compras de un usuario específico", description = "Retorna todas las órdenes generadas por un cliente en orden cronológico descendente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de compras obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Orden.class)),
                            examples = @ExampleObject(name = "Historial de Compras", value = "[\n  {\n    \"id\": 1,\n    \"numeroOrden\": \"#20240705\",\n    \"fecha\": \"2026-11-05T14:30:00\",\n    \"total\": 28775.0,\n    \"estado\": \"PAGADO\",\n    \"metodoPago\": \"Tarjeta Débito Webpay\",\n    \"calle\": \"Los Crisantemos, Edificio Norte\",\n    \"departamento\": \"Depto 603\",\n    \"region\": \"Región Metropolitana de Santiago\",\n    \"comuna\": \"Cerrillos\",\n    \"indicaciones\": \"El martes no estaremos en el depto, pero puede dejárselo con el conserje.\"\n  }\n]")))
    })
    public ResponseEntity<List<Orden>> historialCompras(
            @Parameter(description = "ID del usuario para consultar historial", example = "2", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorUsuario(id));
    }
}
