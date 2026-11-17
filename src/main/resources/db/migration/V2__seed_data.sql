INSERT INTO clientes (nombre, correo, mascota, password, rol) VALUES
                      ('Administrador San Marcos', 'admin@veterinaria.cl', 'Max', 'admin123', 'admin'),
                      ('Juan Pérez', 'juan@duocuc.cl', 'Cachupín', '123456', 'cliente');

INSERT INTO productos (nombre, precio, especie, imagen, descripcion, stock) VALUES
                     ('Alimento Premium Perro Adulto 15kg', 34990, 'Canino', '/img/producto1.avif', 'Nutrición completa y balanceada para perros adultos de todas las razas.', 20),
                     ('Arena Sanitaria Aglomerante 10kg', 12990, 'Felino', '/img/producto2.png', 'Control superior de olores y fácil limpieza para gatos.', 35),
                     ('Antiparasitario Interno Mascotas', 8500, 'Uso General', '/img/producto3.jpg', 'Tableta masticable de amplio espectro para control de parásitos.', 50);

INSERT INTO pedidos (cliente_id, producto_id, cantidad, total, estado) VALUES
    (2, 1, 1, 34990, 'CONFIRMADO');

INSERT INTO mascotas (nombre, especie, raza, edad, cliente_id) VALUES
                                                                   ('Max', 'Perro', 'Golden Retriever', 4, 1),
                                                                   ('Luna', 'Gato', 'Siamés', 2, 1),
                                                                   ('Rocky', 'Perro', 'Bulldog Francés', 1, 2);

INSERT INTO citas (fecha_hora, motivo, estado, mascota_id) VALUES
                                                               ('2026-11-20 10:00:00', 'Vacunación anual y chequeo general', 'PENDIENTE', 1),
                                                               ('2026-11-21 15:30:00', 'Control dermatológico', 'PENDIENTE', 2),
                                                               ('2026-11-15 09:00:00', 'Desparasitación interna', 'ATENDIDO', 3);