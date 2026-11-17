package com.veterinaria.sanmarcos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI veterinariaOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("🐾 API Veterinaria San Marcos")
                        .description("""
                                Plataforma backend RESTful para la gestión de productos veterinarios, clientes y pedidos.
                                
                                ### 🔐 Cómo probar endpoints protegidos:
                                1. Ejecuta **`POST /api/auth/login`** con `admin@veterinaria.cl` / `admin123`.
                                2. Copia el **`token`** recibido.
                                3. Haz clic en el botón verde **Authorize** y pégalo.
                                """)
                        .version("v2.0.0 - Sprint 2")
                        .contact(new Contact()
                                .name("Equipo Veterinaria San Marcos")
                                .email("contacto@veterinariasanmarcos.cl"))
                        .license(new License()
                                .name("Proyecto Académico Duoc UC")
                                .url("http://localhost:8080/swagger-ui.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local de Desarrollo")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Pega aquí tu token JWT obtenido desde /api/auth/login")));
    }

    @Bean
    public SwaggerIndexPageTransformer swaggerIndexPageTransformer(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider) {

        return new SwaggerIndexPageTransformer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider) {
            @Override
            public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
                Resource transformed = super.transform(request, resource, transformerChain);
                if ("index.html".equals(resource.getFilename())) {
                    String html = new String(transformed.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    html = html.replace("</head>", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/swagger-custom.css\"/></head>");
                    return new TransformedResource(transformed, html.getBytes(StandardCharsets.UTF_8));
                }
                return transformed;
            }
        };
    }
}