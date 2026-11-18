# 🐾 BackendSanMarcos — API RESTful Veterinaria San Marcos

Plataforma backend RESTful desarrollada en **Java 17** y **Spring Boot 4.1.2** para la gestión integral de clientes, catálogo de productos veterinarios y pedidos[cite: 2, 13]. Incluye autenticación basada en **JWT**, hipermedia **Spring HATEOAS**, migraciones versionadas con **Flyway** y documentación interactiva personalizada en **Swagger UI**[cite: 1, 2, 4, 17].

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 4.1.2 (Web, Data JPA, Security, HATEOAS)[cite: 13]
* **Base de Datos:** MySQL / MariaDB (XAMPP)
* **Migraciones:** Flyway (`db/migration`)[cite: 1, 17]
* **Seguridad:** Spring Security + JSON Web Tokens (JWT)
* **Documentación:** SpringDoc OpenAPI / Swagger UI (con tema personalizado)[cite: 1, 2, 17]
* **Testing:** JUnit 5, Mockito & DataFaker[cite: 1, 2]

---

## 📂 Estructura del Proyecto

```text
src/
├── main/
│   ├── java/com/veterinaria/sanmarcos/
│   │   ├── config/          # Configuración de OpenAPI/Swagger y Spring Security[cite: 1]
│   │   ├── controller/      # Controladores REST (Auth, Clientes, Productos, Pedidos)[cite: 1]
│   │   ├── dto/             # Objetos de transferencia de datos (Login, AuthResponse, etc.)[cite: 1]
│   │   ├── entity/          # Entidades JPA (Cliente, Producto, Pedido)[cite: 1]
│   │   ├── repository/      # Repositorios Spring Data JPA[cite: 1]
│   │   ├── security/        # JwtUtil y JwtFilter[cite: 1]
│   │   └── service/         # Lógica de negocio[cite: 1]
│   └── resources/
│       ├── db/migration/    # Scripts SQL de Flyway (V1 schema, V2 seed data)[cite: 1, 13]
│       ├── static/          # Estilos personalizados (swagger-custom.css)[cite: 1]
│       └── application.properties[cite: 1]
└── test/                    # Pruebas unitarias de servicios[cite: 1]
