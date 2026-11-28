# 🐾 BackendSanMarcos — API RESTful Veterinaria San Marcos

Plataforma backend RESTful desarrollada en **Java 17** y **Spring Boot 4.1.2** para la gestión integral de clientes, catálogo de productos veterinarios, pedidos, mascotas y citas médicas. Incluye autenticación basada en **JWT**, migraciones versionadas con **Flyway** y documentación interactiva en **Swagger UI**.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 4.1.2 (Spring Web, Spring Data JPA, Spring Security)
- **Base de Datos:** MySQL / MariaDB (XAMPP)
- **Migraciones:** Flyway (`src/main/resources/db/migration`)
- **Seguridad:** Spring Security + JSON Web Tokens (JWT)
- **Documentación:** SpringDoc OpenAPI / Swagger UI (con tema personalizado)
- **Testing:** JUnit 5, Mockito y DataFaker

---

## 📂 Estructura del Proyecto

```text
src/
├── main/
│   ├── java/com/veterinaria/sanmarcos/
│   │   ├── config/          # Configuración de OpenAPI/Swagger, CORS y Spring Security
│   │   ├── controller/      # Controladores REST (Auth, Clientes, Productos, Pedidos, Mascotas, Citas)
│   │   ├── dto/             # Objetos de transferencia de datos (Login, AuthResponse, etc.)
│   │   ├── entity/          # Entidades JPA (Cliente, Producto, Pedido, Mascota, Cita)
│   │   ├── repository/      # Repositorios Spring Data JPA
│   │   ├── security/        # JwtUtil y JwtFilter
│   │   └── service/         # Lógica de negocio
│   └── resources/
│       ├── db/migration/    # Scripts SQL de Flyway (V1__create_schema.sql, V2__seed_data.sql)
│       ├── static/          # Estilos personalizados (swagger-custom.css)
│       └── application.properties
└── test/                    # Pruebas unitarias de servicios
```
## ✨ Funcionalidades y Base de Datos
- **Entidades y migraciones Flyway:** esquema inicial (V1) y datos de prueba (V2) para clientes, productos, pedidos, mascotas y citas en MySQL.
- **Autenticación y control de roles:** emisión y validación de tokens JWT mediante Spring Security (`JwtFilter` y `SecurityConfig`), restringiendo los endpoints de citas (`/api/v1/citas/**`) y acciones administrativas al rol ADMIN.
- **API RESTful y Swagger UI:** endpoints para autenticación, consulta y administración de recursos, disponibles en http://localhost:8080 y documentados en Swagger UI.
- **Pruebas unitarias:** suite de pruebas con JUnit 5, Mockito y DataFaker para validar la lógica de negocio.

## ⚙️ Variables de Entorno
`src/main/resources/application.properties` lee variables de entorno con valores por defecto para desarrollo local:

- `DB_URL` (por defecto: `jdbc:mysql://localhost:3306/veterinaria_sanmarcos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`)
- `DB_USERNAME` (por defecto: root)
- `DB_PASSWORD` (por defecto: vacío)
- `JWT_SECRET` (por defecto: valor de ejemplo para desarrollo; definir una clave segura en otros entornos)

## 🚀 Ejecución Local (IntelliJ IDEA + MySQL)
- Verifica que MySQL esté iniciado (por ejemplo desde XAMPP) y crea o confirma la base de datos configurada.
- Abre el proyecto en IntelliJ IDEA, espera a que carguen las dependencias y ejecuta `SanmarcosApplication`.
- Comprueba en la consola que el servidor levante en `http://localhost:8080` y que Flyway aplique las migraciones sin errores.
- Accede a Swagger UI en el navegador (`http://localhost:8080/swagger-ui/index.html`) para revisar y probar los endpoints.
- Para ejecutar las pruebas unitarias desde terminal:

```bash
./mvnw test
```

## 🔐 Cuentas de Prueba (Datos Iniciales)
- Administrador (admin): admin@veterinaria.cl / admin123
- Cliente (cliente): juan@duocuc.cl / 123456
