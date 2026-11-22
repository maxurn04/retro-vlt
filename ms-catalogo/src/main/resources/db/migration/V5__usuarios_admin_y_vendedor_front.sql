INSERT INTO usuarios (nombre, apellidos, correo, password, rol)
SELECT 'Administrador', 'RetroVLT', 'admin@gmail.com', 'admin.2026', 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'admin@gmail.com');

INSERT INTO usuarios (nombre, apellidos, correo, password, rol)
SELECT 'Vendedor', 'Uno', 'vendedor1@gmail.com', 'vend.2026', 'VENDEDOR'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'vendedor1@gmail.com');

UPDATE usuarios SET password = 'vend.2026', rol = 'VENDEDOR' WHERE correo = 'vendedor1@gmail.com';