-- V2__seed_initial_data.sql
-- Datos iniciales basados en la evaluación DSY1104 y backend-explorer.html

-- 1. Categorías
INSERT INTO categorias (id, nombre, descripcion, imagen_url) VALUES
(1, 'Videojuegos', 'Juegos para PC, PlayStation, Xbox y Nintendo', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600'),
(2, 'Consolas', 'Consolas de nueva generación y ediciones retro', 'https://images.unsplash.com/photo-1486401899868-0e435ed85128?w=600'),
(3, 'Accesorios', 'Mandos, teclados, auriculares y periféricos', 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600'),
(4, 'Retro', 'Clásicos arcade y consolas legendarias', 'https://images.unsplash.com/photo-1551103782-8ab07afd45c1?w=600');

-- 2. Productos
INSERT INTO productos (id, nombre, descripcion, precio, stock, stock_critico, es_oferta, precio_oferta, imagen_url, categoria_id) VALUES
(1, 'The Witcher 3: Wild Hunt', 'Juego de rol y acción en mundo abierto de fantasía oscura.', 3999, 15, 3, FALSE, NULL, 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=500', 1),
(2, 'Minecraft', 'Construye y explora tu propio mundo con bloques infinitos.', 2695, 4, 5, TRUE, 1990, 'https://images.unsplash.com/photo-1627856013091-fed6e4e30025?w=500', 1),
(3, 'Red Dead Redemption 2', 'Una épica historia de forajidos en el salvaje oeste americano.', 5999, 8, 3, FALSE, NULL, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500', 1),
(4, 'Fortnite Skin Pack', 'Paquete de apariencias exclusivas y pavos para Battle Royale.', 0, 1, 5, FALSE, NULL, 'https://images.unsplash.com/photo-1589241062272-c0a000072dfa?w=500', 1),
(5, 'Hollow Knight', 'Aventura de acción metroidvania en las profundidades de Hallownest.', 1499, 12, 3, FALSE, NULL, 'https://images.unsplash.com/photo-1579373903781-fd5c0c30c4cd?w=500', 1),
(6, 'Among Us Collector', 'Juego de misterio y deducción espacial para jugar con amigos.', 499, 2, 4, FALSE, NULL, 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500', 1),
(7, 'Animal Crossing: New Horizons', 'Crea tu paraíso isleño personalizado a tu propio ritmo.', 5999, 10, 4, FALSE, NULL, 'https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=500', 1),
(8, 'Ghost of Tsushima', 'Conviértete en el Fantasma para salvar a Tsushima de los invasores.', 3999, 1, 5, FALSE, NULL, 'https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=500', 1);

-- 3. Usuarios (Admin y Cliente del PDF)
INSERT INTO usuarios (id, nombre, apellidos, correo, password, rol, telefono, direccion_defecto) VALUES
(1, 'Administrador', 'RetroVLT', 'admin@tienda.cl', 'admin123', 'ADMIN', '+56911223344', 'Av. Providencia 1234'),
(2, 'Pedro', 'Hacker', 'pedro.hacker20@example.com', '123456', 'CLIENTE', '+56999887766', 'Los Crisantemos, Edificio Norte, Depto 603, Cerrillos');

-- 4. Ordenes (Boleta #20240705 que sale en la Figura 6, 7 y 8 del PDF)
INSERT INTO ordenes (id, numero_orden, fecha, total, estado, usuario_id, calle, departamento, region, comuna, indicaciones) VALUES
(1, '#20240705', '2026-11-05 14:30:00', 28775, 'PAGADO', 2, 'Los Crisantemos, Edificio Norte', 'Depto 603', 'Región Metropolitana de Santiago', 'Cerrillos', 'El martes no estaremos en el depto, pero puede dejárselo con el conserje.');

-- 5. Detalle de la orden #20240705 (Ítems de la pantalla de checkout: Minecraft x4, RDR2 x1, Witcher 3 x1, etc.)
INSERT INTO detalle_orden (id, orden_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 4, 1, 0, 0),         -- Fortnite x1 ($0)
(2, 1, 2, 4, 2695, 10780),  -- Minecraft x4 ($10780)
(3, 1, 3, 1, 5999, 5999),   -- Red Dead 2 x1 ($5999)
(4, 1, 6, 1, 499, 499),     -- Among Us x1 ($499)
(5, 1, 1, 1, 3999, 3999),   -- The Witcher 3 x1 ($3999)
(6, 1, 5, 1, 1499, 1499),   -- Hollow Knight x1 ($1499)
(7, 1, 7, 1, 5999, 5999);   -- Animal Crossing x1 ($5999)
-- Total suma: 0 + 10780 + 5999 + 499 + 3999 + 1499 + 5999 = 28775 (Exacto al del PDF)

-- 6. Blogs
INSERT INTO blogs (id, titulo, contenido, fecha_publicacion, autor, imagen_url) VALUES
(1, 'Top 10 Juegos Retro de la década', 'Un repaso imperdible por los títulos que definieron una generación de jugadores...', '2026-11-01', 'Equipo Retro-VLT', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600'),
(2, 'Cómo armar tu setup gamer con poco presupuesto', 'Consejos prácticos para optimizar tus periféricos y disfrutar de tus videojuegos preferidos...', '2026-11-03', 'Admin', 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600');
