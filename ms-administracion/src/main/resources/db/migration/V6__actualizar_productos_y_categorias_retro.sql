UPDATE categorias SET nombre = 'Consolas', descripcion = 'Consolas retro clásicas y legendarias', imagen_url = '/img/ps2.jpg' WHERE id = 1;
UPDATE categorias SET nombre = 'Juegos', descripcion = 'Videojuegos clásicos que marcaron una era', imagen_url = '/img/zelda64.webp' WHERE id = 2;
UPDATE categorias SET nombre = 'Accesorios', descripcion = 'Mandos, memorias y periféricos originales', imagen_url = '/img/dualshock2.avif' WHERE id = 3;
UPDATE categorias SET nombre = 'Retro', descripcion = 'Ediciones de colección y clásicos gamer', imagen_url = '/img/DREAMCAST.webp' WHERE id = 4;

UPDATE productos SET 
    codigo = 'BAR000001',
    nombre = 'PlayStation 2',
    descripcion = 'La PlayStation 2 (PS2) es una consola de videojuegos de sexta generación desarrollada por Sony Computer Entertainment. Fue lanzada en marzo de 2000 y se convirtió en una de las consolas más exitosas de todos los tiempos, con una amplia biblioteca de juegos y compatibilidad con juegos de la PlayStation original.',
    precio = 60000,
    stock = 4,
    stock_critico = 2,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/ps2.jpg',
    img2 = '/img/ps2-2.jpg',
    img3 = NULL,
    categoria_id = 1
WHERE id = 1;

UPDATE productos SET 
    codigo = 'BAR000002',
    nombre = 'Zelda 64',
    descripcion = 'Zelda 64 es un videojuego de acción y aventura desarrollado por Nintendo. Fue lanzado en 1998 para la Nintendo 64 y se considera uno de los mejores juegos de la serie.',
    precio = 30000,
    stock = 5,
    stock_critico = 2,
    es_oferta = TRUE,
    precio_oferta = 24000,
    imagen_url = '/img/zelda64.webp',
    img2 = '/img/zelda64-2.jpg',
    img3 = '/img/zelda64-3.jpg',
    categoria_id = 2
WHERE id = 2;

UPDATE productos SET 
    codigo = 'BAR000003',
    nombre = 'Nintendo 64',
    descripcion = 'La consola Nintendo 64 N64 usada ofrece acceso a una gran variedad de juegos icónicos que definieron una generación en el entretenimiento digital. Su diseño clásico mantiene la funcionalidad original, permitiendo disfrutar de títulos que aún son referencia en el mundo gamer.',
    precio = 50000,
    stock = 8,
    stock_critico = 3,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/N64.avif',
    img2 = '/img/bolsoEjemplo-2.webp',
    img3 = NULL,
    categoria_id = 1
WHERE id = 3;

UPDATE productos SET 
    codigo = 'BAR000004',
    nombre = 'PlayStation 3',
    descripcion = 'La PlayStation 3 (PS3) Slim es una consola de videojuegos de séptima generación desarrollada por Sony Computer Entertainment. Fue lanzada en 2006 y se caracterizó por su potente hardware y soporte para el formato Blu-ray.',
    precio = 135000,
    stock = 2,
    stock_critico = 1,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/ps3.jpg',
    img2 = '/img/ps3-2.jpg',
    img3 = '/img/ps3-3.jpg',
    categoria_id = 1
WHERE id = 4;

UPDATE productos SET 
    codigo = 'BAR000005',
    nombre = 'Zeebo',
    descripcion = 'Zeebo es una consola de videojuegos diseñada para ser utilizada con un televisor estándar. Fue desarrollada por Activision y lanzada en 2011.',
    precio = 35000,
    stock = 2,
    stock_critico = 1,
    es_oferta = TRUE,
    precio_oferta = 29000,
    imagen_url = '/img/zeebo.jpeg',
    img2 = '/img/zeebo-2.jpeg',
    img3 = NULL,
    categoria_id = 1
WHERE id = 5;

UPDATE productos SET 
    codigo = 'BAR000006',
    nombre = 'GameCube',
    descripcion = 'El GameCube es una consola de videojuegos desarrollada por Nintendo. Fue lanzada en 2001 y se caracterizó por su diseño compacto y su control innovador.',
    precio = 40000,
    stock = 1,
    stock_critico = 1,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/gamecub.jpg',
    img2 = '/img/gamecub-2.webp',
    img3 = '/img/gamecub-3.jpg',
    categoria_id = 1
WHERE id = 6;

UPDATE productos SET 
    codigo = 'BAR000007',
    nombre = 'Super Mario 64',
    descripcion = 'Clásico juego de plataformas 3D que definió una era. Explora el castillo de la Princesa Peach y recupera las estrellas de poder.',
    precio = 55000,
    stock = 10,
    stock_critico = 3,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/MARIO64.webp',
    img2 = '/img/sm64-2.jpg',
    img3 = '/img/sm64-3.webp',
    categoria_id = 2
WHERE id = 7;

UPDATE productos SET 
    codigo = 'BAR000008',
    nombre = 'Mando DualShock 2',
    descripcion = 'Control original para PlayStation 2, color negro. Precisión y comodidad con vibración integrada para una mejor experiencia.',
    precio = 15000,
    stock = 15,
    stock_critico = 5,
    es_oferta = FALSE,
    precio_oferta = NULL,
    imagen_url = '/img/dualshock2.avif',
    img2 = '/img/dualshock2-2.jpg',
    img3 = '/img/dualshock-3.jpg',
    categoria_id = 3
WHERE id = 8;

INSERT INTO productos (id, codigo, nombre, descripcion, precio, stock, stock_critico, es_oferta, precio_oferta, imagen_url, img2, img3, categoria_id) VALUES
(9, 'BAR000009', 'Sega Dreamcast', 'La última consola de Sega. Pionera en el juego en línea y con un catálogo lleno de innovadoras joyas arcade.', 115000, 3, 1, FALSE, NULL, '/img/DREAMCAST.webp', '/img/dreamcast-2.jpg', '/img/dreamcast-3.webp', 1),
(10, 'BAR000010', 'Tira de Luces RGB 5m', 'Ilumina tu setup gamer con esta tira LED RGB de 5 metros. Control remoto incluido con múltiples modos de iluminación para darle estilo a tu pieza.', 12000, 20, 5, TRUE, 9990, '/img/lucesledrgb.webp', '/img/tiraled-2.jpg', '/img/tiraled-3.jpg', 3),
(11, 'BAR000011', 'Halo: Combat Evolved', 'El revolucionario shooter en primera persona que lanzó al estrellato a la primera consola Xbox. Únete al Jefe Maestro en su épica lucha.', 30000, 7, 2, FALSE, NULL, '/img/haloCE.webp', '/img/haloCE-2.png', '/img/haloCE-3.jpg', 2),
(12, 'BAR000012', 'Memory Card PS2 8MB', 'Tarjeta de memoria oficial de 8MB para PlayStation 2. Guarda todas tus partidas y no pierdas tu progreso en tus juegos favoritos.', 15000, 12, 4, FALSE, NULL, '/img/MEMORYCARD.jpg', '/img/memorycard-2.jpg', '/img/memorycard-3.webp', 3)
ON DUPLICATE KEY UPDATE 
    codigo = VALUES(codigo),
    nombre = VALUES(nombre),
    descripcion = VALUES(descripcion),
    precio = VALUES(precio),
    stock = VALUES(stock),
    stock_critico = VALUES(stock_critico),
    es_oferta = VALUES(es_oferta),
    precio_oferta = VALUES(precio_oferta),
    imagen_url = VALUES(imagen_url),
    img2 = VALUES(img2),
    img3 = VALUES(img3),
    categoria_id = VALUES(categoria_id);
