CREATE TABLE clientes (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          correo VARCHAR(120) NOT NULL UNIQUE,
                          mascota VARCHAR(100),
                          password VARCHAR(255) NOT NULL,
                          rol VARCHAR(20) NOT NULL DEFAULT 'cliente'
);

CREATE TABLE productos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(150) NOT NULL,
                           precio INT NOT NULL,
                           especie VARCHAR(50) NOT NULL,
                           imagen TEXT,
                           descripcion VARCHAR(500),
                           stock INT NOT NULL DEFAULT 10
);

CREATE TABLE pedidos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         cliente_id BIGINT NOT NULL,
                         producto_id BIGINT NOT NULL,
                         cantidad INT NOT NULL DEFAULT 1,
                         total INT NOT NULL,
                         estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
                         fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                         CONSTRAINT fk_pedido_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE mascotas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          especie VARCHAR(50) NOT NULL,
                          raza VARCHAR(100),
                          edad INT NOT NULL,
                          cliente_id BIGINT NOT NULL,
                          CONSTRAINT fk_mascota_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE citas (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       fecha_hora DATETIME NOT NULL,
                       motivo VARCHAR(255) NOT NULL,
                       estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
                       mascota_id BIGINT NOT NULL,
                       CONSTRAINT fk_cita_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE
);