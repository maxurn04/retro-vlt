-- V4__add_frontend_fields.sql
-- Agrega columnas necesarias para la tienda y administración Retro-VLT

-- 1. Campos adicionales para productos
ALTER TABLE productos ADD COLUMN codigo VARCHAR(50);
ALTER TABLE productos ADD COLUMN img2 VARCHAR(255);
ALTER TABLE productos ADD COLUMN img3 VARCHAR(255);

-- Asignar códigos estándar a productos existentes (BAR000001, BAR000002, etc.)
UPDATE productos SET codigo = CONCAT('BAR', LPAD(id, 6, '0')) WHERE codigo IS NULL;

-- 2. Campos adicionales para usuarios (compatibilidad formulario chileno de registro)
ALTER TABLE usuarios ADD COLUMN run VARCHAR(20);
ALTER TABLE usuarios ADD COLUMN fecha_nacimiento VARCHAR(30);
ALTER TABLE usuarios ADD COLUMN region VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN comuna VARCHAR(100);

-- 3. Método de pago en órdenes / boletas
ALTER TABLE ordenes ADD COLUMN metodo_pago VARCHAR(50);
UPDATE ordenes SET metodo_pago = 'TARJETA_DEBITO' WHERE metodo_pago IS NULL;
