-- V3__update_clients.sql
-- Elimina a Pedro Hacker y agrega 2 usuarios con rol CLIENTE

-- 1. Insertar 2 nuevos clientes
INSERT INTO usuarios (id, nombre, apellidos, correo, password, rol, telefono, direccion_defecto) VALUES
(3, 'Carlos', 'Mendoza', 'carlos.mendoza@gmail.com', 'carlos123', 'CLIENTE', '+56987654321', 'Av. Las Condes 1234, Depto 401, Las Condes'),
(4, 'Valentina', 'Rojas', 'valentina.rojas@gmail.com', 'valen2026', 'CLIENTE', '+56976543210', 'Calle Los Olivos 567, Maipú');

-- 2. Reasignar cualquier orden existente al nuevo cliente Carlos Mendoza
UPDATE ordenes SET usuario_id = 3 WHERE usuario_id = 2;

-- 3. Eliminar a Pedro Hacker
DELETE FROM usuarios WHERE id = 2;
