-- V8: Actualizar credenciales de administrador a admin@gmail.com / admin
UPDATE usuarios SET password = 'admin', rol = 'ADMIN' WHERE correo = 'admin@gmail.com';

INSERT INTO usuarios (nombre, apellidos, correo, password, rol, telefono, direccion_defecto)
SELECT 'Administrador', 'RetroVLT', 'admin@gmail.com', 'admin', 'ADMIN', '+56911223344', 'Av. Providencia 1234'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'admin@gmail.com');
