package com.back.retro_vlt.controller;

import com.back.retro_vlt.dto.GoogleAuthDto;
import com.back.retro_vlt.dto.LoginDto;
import com.back.retro_vlt.model.Usuario;
import com.back.retro_vlt.security.JwtUtil;
import com.back.retro_vlt.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Tienda - Autenticación & Clientes", description = "Endpoints para registro, inicio de sesión (local y Google OAuth2), validación de tokens JWT y consulta de perfil")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar una nueva cuenta de cliente",
            description = "Crea un nuevo usuario en la base de datos con rol CLIENTE por defecto. Valida que el correo no se encuentre registrado previamente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Registro Exitoso Cliente",
                                            summary = "Cliente registrado satisfactoriamente",
                                            value = "{\n  \"id\": 5,\n  \"run\": \"19876543-2\",\n  \"nombre\": \"Carlos\",\n  \"apellidos\": \"Mendoza\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"password\": \"Pass123*\",\n  \"rol\": \"CLIENTE\",\n  \"telefono\": \"+56987654321\",\n  \"fechaNacimiento\": \"1998-05-20\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Las Condes\",\n  \"direccionDefecto\": \"Av. Las Condes 1234, Depto 401\"\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o correo electrónico ya registrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Correo Duplicado",
                                            summary = "Error cuando el correo ya existe",
                                            value = "{\n  \"error\": \"No se pudo registrar el usuario. El correo ya existe o los datos son inválidos.\"\n}"
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<?> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos completos del nuevo usuario a registrar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo Cliente Duoc",
                                            summary = "Registro completo de usuario cliente",
                                            value = "{\n  \"run\": \"19876543-2\",\n  \"nombre\": \"Carlos\",\n  \"apellidos\": \"Mendoza\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"password\": \"Pass123*\",\n  \"telefono\": \"+56987654321\",\n  \"fechaNacimiento\": \"1998-05-20\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Las Condes\",\n  \"direccionDefecto\": \"Av. Las Condes 1234, Depto 401\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Ejemplo Usuario Admin",
                                            summary = "Registro de usuario con rol ADMIN",
                                            value = "{\n  \"run\": \"11222333-4\",\n  \"nombre\": \"Administrador\",\n  \"apellidos\": \"RetroVLT\",\n  \"correo\": \"admin@gmail.com\",\n  \"password\": \"admin\",\n  \"rol\": \"ADMIN\",\n  \"telefono\": \"+56911223344\",\n  \"fechaNacimiento\": \"1990-01-15\",\n  \"region\": \"Región Metropolitana de Santiago\",\n  \"comuna\": \"Santiago\",\n  \"direccionDefecto\": \"Av. Providencia 1234\"\n}"
                                    )
                            }
                    )
            )
            @RequestBody Usuario usuario
    ) {
        Usuario nuevo = usuarioService.registrar(usuario);
        if (nuevo == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar el usuario. El correo ya existe o los datos son inválidos."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión con correo y contraseña generando token JWT",
            description = "Valida las credenciales ingresadas. Si son correctas, genera y retorna un token JWT válido junto con el perfil completo del usuario autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación satisfactoria y token JWT generado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Login Exitoso Admin",
                                            summary = "Respuesta de login para cuenta administrador",
                                            value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2wiOiJBRE1JTiJ9...\",\n  \"tipo\": \"Bearer\",\n  \"id\": 1,\n  \"nombre\": \"Administrador\",\n  \"apellidos\": \"RetroVLT\",\n  \"correo\": \"admin@gmail.com\",\n  \"rol\": \"ADMIN\",\n  \"tipo\": \"ADMIN\",\n  \"telefono\": \"+56911223344\",\n  \"direccionDefecto\": \"Av. Providencia 1234\",\n  \"usuario\": {\n    \"id\": 1,\n    \"run\": \"\",\n    \"nombre\": \"Administrador\",\n    \"apellidos\": \"RetroVLT\",\n    \"correo\": \"admin@gmail.com\",\n    \"rol\": \"ADMIN\",\n    \"tipo\": \"ADMIN\",\n    \"telefono\": \"+56911223344\",\n    \"region\": \"\",\n    \"comuna\": \"\",\n    \"direccionDefecto\": \"Av. Providencia 1234\",\n    \"direccion\": \"Av. Providencia 1234\"\n  }\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Login Exitoso Cliente",
                                            summary = "Respuesta de login para cliente estándar",
                                            value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnRlQGR1b2MuY2wiLCJyb2wiOiJDTElFTlRFIn0...\",\n  \"tipo\": \"Bearer\",\n  \"id\": 3,\n  \"nombre\": \"Carlos\",\n  \"apellidos\": \"Mendoza\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"rol\": \"CLIENTE\",\n  \"tipo\": \"CLIENTE\",\n  \"telefono\": \"+56987654321\",\n  \"direccionDefecto\": \"Av. Las Condes 1234, Depto 401, Las Condes\",\n  \"usuario\": {\n    \"id\": 3,\n    \"run\": \"19876543-2\",\n    \"nombre\": \"Carlos\",\n    \"apellidos\": \"Mendoza\",\n    \"correo\": \"cliente@duoc.cl\",\n    \"rol\": \"CLIENTE\",\n    \"tipo\": \"CLIENTE\",\n    \"telefono\": \"+56987654321\",\n    \"region\": \"Región Metropolitana de Santiago\",\n    \"comuna\": \"Las Condes\",\n    \"direccionDefecto\": \"Av. Las Condes 1234, Depto 401, Las Condes\",\n    \"direccion\": \"Av. Las Condes 1234, Depto 401, Las Condes\"\n  }\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas (correo o contraseña no coinciden)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "No Autorizado",
                                            summary = "Error de autenticación",
                                            value = "{\n  \"error\": \"Credenciales incorrectas\"\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cuerpo de la petición nulo o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Datos Faltantes",
                                            summary = "Error por payload vacío",
                                            value = "{\n  \"error\": \"Datos de acceso requeridos\"\n}"
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de acceso con correo y contraseña",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Credenciales Admin",
                                            summary = "Login como Administrador del sistema",
                                            value = "{\n  \"correo\": \"admin@gmail.com\",\n  \"password\": \"admin\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Credenciales Cliente Duoc",
                                            summary = "Login como Cliente registrado",
                                            value = "{\n  \"correo\": \"cliente@duoc.cl\",\n  \"password\": \"Pass123*\"\n}"
                                    )
                            }
                    )
            )
            @RequestBody LoginDto dto
    ) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos de acceso requeridos"));
        }
        Usuario usuario = usuarioService.login(dto.getCorreo(), dto.getPassword());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
        String token = jwtUtil.generarToken(usuario);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        resp.put("tipo", usuario.getRol());
        resp.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        resp.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/google")
    @Operation(
            summary = "Iniciar sesión o registrarse con cuenta de Google (OAuth2 / Google Cloud)",
            description = "Recibe el token o credencial emitido por Google Identity Services. Si el usuario no existe en la base de datos se registra automáticamente con rol CLIENTE. Retorna token JWT propio de la plataforma y el perfil del usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación con Google exitosa y sesión iniciada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Google Login Exitoso",
                                            summary = "Sesión iniciada vía Google OAuth2",
                                            value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnRlQGR1b2MuY2wiLCJyb2wiOiJDTElFTlRFIn0...\",\n  \"tipo\": \"Bearer\",\n  \"id\": 6,\n  \"nombre\": \"Carlos Mendoza\",\n  \"apellidos\": \"\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"rol\": \"CLIENTE\",\n  \"tipo\": \"CLIENTE\",\n  \"telefono\": \"\",\n  \"direccionDefecto\": \"\",\n  \"usuario\": {\n    \"id\": 6,\n    \"run\": \"\",\n    \"nombre\": \"Carlos Mendoza\",\n    \"apellidos\": \"\",\n    \"correo\": \"cliente@duoc.cl\",\n    \"rol\": \"CLIENTE\",\n    \"tipo\": \"CLIENTE\",\n    \"telefono\": \"\",\n    \"region\": \"\",\n    \"comuna\": \"\",\n    \"direccionDefecto\": \"\",\n    \"direccion\": \"\"\n  }\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido o correo electrónico no recuperable del token Google",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Error Correo Faltante",
                                            summary = "Google no proveyó email",
                                            value = "{\n  \"error\": \"No se pudo obtener el correo de la cuenta de Google\"\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al procesar la autenticación de Google",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Error Interno",
                                            summary = "Fallo en persistencia o creación de usuario",
                                            value = "{\n  \"error\": \"Error al procesar el inicio de sesión con Google\"\n}"
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<?> loginGoogle(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credencial JWT o token emitido por el SDK de Google Sign-In",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GoogleAuthDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Google Credential Completo",
                                            summary = "Token JWT de Google con datos de perfil",
                                            value = "{\n  \"credential\": \"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImNsaWVudGVAZHVvYy5jbCIsIm5hbWUiOiJDYXJsb3MgTWVuZG96YSJ9...\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"nombre\": \"Carlos Mendoza\",\n  \"picture\": \"https://lh3.googleusercontent.com/a/ACg8ocI...\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Google Auth Directo",
                                            summary = "Payload con correo y nombre verificados",
                                            value = "{\n  \"token\": \"ya29.a0AfH6SMBx...\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"nombre\": \"Carlos Mendoza\"\n}"
                                    )
                            }
                    )
            )
            @RequestBody GoogleAuthDto dto
    ) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos de autenticación de Google requeridos"));
        }

        String googleToken = dto.getCredential() != null && !dto.getCredential().isBlank()
                ? dto.getCredential()
                : dto.getToken();

        String correo = dto.getCorreo();
        String nombre = dto.getNombre();

        if (googleToken != null && !googleToken.isBlank()) {
            Map<String, Object> payload = jwtUtil.obtenerPayload(googleToken);
            if (payload != null) {
                if (payload.containsKey("email")) {
                    correo = (String) payload.get("email");
                } else if (payload.containsKey("sub") && correo == null) {
                    correo = (String) payload.get("sub");
                }
                if (payload.containsKey("name")) {
                    nombre = (String) payload.get("name");
                }
            }
        }

        if (correo == null || correo.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo obtener el correo de la cuenta de Google"));
        }

        if (nombre == null || nombre.isBlank()) {
            nombre = "Usuario Google";
        }

        Usuario usuario = usuarioService.loginOauthGoogle(correo, nombre);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el inicio de sesión con Google"));
        }

        String token = jwtUtil.generarToken(usuario);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        resp.put("tipo", usuario.getRol());
        resp.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        resp.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        return ResponseEntity.ok(resp);
    }

    @GetMapping({"/validar", "/perfil"})
    @Operation(
            summary = "Validar token JWT y consultar perfil del usuario autenticado",
            description = "Valida el encabezado Authorization con esquema 'Bearer <token>'. Si el token es íntegro y no ha expirado, responde con confirmación de validez y los datos del perfil del usuario registrado.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token válido y datos del usuario autenticado obtenidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Token Válido Cliente",
                                            summary = "Sesión activa de usuario cliente",
                                            value = "{\n  \"valido\": true,\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnRlQGR1b2MuY2wiLCJyb2wiOiJDTElFTlRFIn0...\",\n  \"tipo\": \"Bearer\",\n  \"id\": 3,\n  \"nombre\": \"Carlos\",\n  \"correo\": \"cliente@duoc.cl\",\n  \"rol\": \"CLIENTE\",\n  \"usuario\": {\n    \"id\": 3,\n    \"run\": \"19876543-2\",\n    \"nombre\": \"Carlos\",\n    \"apellidos\": \"Mendoza\",\n    \"correo\": \"cliente@duoc.cl\",\n    \"rol\": \"CLIENTE\",\n    \"tipo\": \"CLIENTE\",\n    \"telefono\": \"+56987654321\",\n    \"region\": \"Región Metropolitana de Santiago\",\n    \"comuna\": \"Las Condes\",\n    \"direccionDefecto\": \"Av. Las Condes 1234, Depto 401\",\n    \"direccion\": \"Av. Las Condes 1234, Depto 401\"\n  }\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Token Válido Admin",
                                            summary = "Sesión activa de usuario administrador",
                                            value = "{\n  \"valido\": true,\n  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2wiOiJBRE1JTiJ9...\",\n  \"tipo\": \"Bearer\",\n  \"id\": 1,\n  \"nombre\": \"Administrador\",\n  \"correo\": \"admin@gmail.com\",\n  \"rol\": \"ADMIN\",\n  \"usuario\": {\n    \"id\": 1,\n    \"run\": \"\",\n    \"nombre\": \"Administrador\",\n    \"apellidos\": \"RetroVLT\",\n    \"correo\": \"admin@gmail.com\",\n    \"rol\": \"ADMIN\",\n    \"tipo\": \"ADMIN\",\n    \"telefono\": \"+56911223344\",\n    \"region\": \"\",\n    \"comuna\": \"\",\n    \"direccionDefecto\": \"Av. Providencia 1234\",\n    \"direccion\": \"Av. Providencia 1234\"\n  }\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, inválido, corrupto o expirado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Token No Proporcionado",
                                            summary = "Encabezado Authorization faltante o sin prefijo Bearer",
                                            value = "{\n  \"error\": \"Token no proporcionado\"\n}"
                                    ),
                                    @ExampleObject(
                                            name = "Token Inválido o Expirado",
                                            summary = "Firma inválida o tiempo de vida expirado",
                                            value = "{\n  \"error\": \"Token inválido o expirado\"\n}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario asociado al correo del token no existe en el sistema",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Usuario No Encontrado",
                                            summary = "El usuario fue eliminado o no existe en la base de datos",
                                            value = "{\n  \"error\": \"Usuario no encontrado\"\n}"
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<?> validarSesion(
            @Parameter(
                    name = "Authorization",
                    description = "Token de autorización JWT en formato 'Bearer <token>'",
                    in = ParameterIn.HEADER,
                    required = false,
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnRlQGR1b2MuY2wiLCJyb2wiOiJDTElFTlRFIiwiZXhwIjoxNzMxMjM0NTY3fQ.signature..."
            )
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token no proporcionado"));
        }
        String token = authHeader.substring(7).trim();
        if (!jwtUtil.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o expirado"));
        }
        String correo = jwtUtil.obtenerCorreo(token);
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("valido", true);
        resp.put("token", token);
        resp.put("tipo", "Bearer");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("run", usuario.getRun() != null ? usuario.getRun() : "");
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("apellidos", usuario.getApellidos() != null ? usuario.getApellidos() : "");
        usuarioData.put("correo", usuario.getCorreo());
        usuarioData.put("rol", usuario.getRol());
        usuarioData.put("tipo", usuario.getRol());
        usuarioData.put("direccionDefecto", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("direccion", usuario.getDireccionDefecto() != null ? usuario.getDireccionDefecto() : "");
        usuarioData.put("telefono", usuario.getTelefono() != null ? usuario.getTelefono() : "");
        usuarioData.put("region", usuario.getRegion() != null ? usuario.getRegion() : "");
        usuarioData.put("comuna", usuario.getComuna() != null ? usuario.getComuna() : "");

        resp.put("usuario", usuarioData);
        resp.put("id", usuario.getId());
        resp.put("nombre", usuario.getNombre());
        resp.put("correo", usuario.getCorreo());
        resp.put("rol", usuario.getRol());
        return ResponseEntity.ok(resp);
    }
}
