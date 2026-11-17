INSERT INTO clientes (nombre, correo, mascota, password, rol) VALUES
                      ('Administrador San Marcos', 'admin@veterinaria.cl', 'Max', 'admin123', 'admin'),
                      ('Juan Pérez', 'juan@duocuc.cl', 'Cachupín', '123456', 'cliente');

INSERT INTO productos (nombre, precio, especie, imagen, descripcion, stock) VALUES
                     ('Alimento Premium Perro Adulto 15kg', 34990, 'Canino', '/img/producto1.avif', 'Nutrición completa y balanceada para perros adultos de todas las razas.', 20),
                     ('Arena Sanitaria Aglomerante 10kg', 12990, 'Felino', '/img/producto2.png', 'Control superior de olores y fácil limpieza para gatos.', 35),
                     ('Antiparasitario Interno Mascotas', 8500, 'Uso General', '/img/producto3.jpg', 'Tableta masticable de amplio espectro para control de parásitos.', 50);

INSERT INTO pedidos (cliente_id, producto_id, cantidad, total, estado) VALUES
    (2, 1, 1, 34990, 'CONFIRMADO');