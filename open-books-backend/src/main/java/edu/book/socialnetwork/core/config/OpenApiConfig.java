package edu.book.socialnetwork.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Open Books API",
                        email = "sadeepahearth@gmail.com",
                        url = "www.linkedin.com/in/sadeepa-herath-02ab67310"
                ),
                description = "Open Books API documentation",
                title = "OpenAPI Specification - Open Books ",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Localhost Server",
                        url = "http://localhost:8088/api/v1"
                )/*,
                @Server(
                        description = "Production Server",
                        url = "https://api.open-books.com/api/v1"
                )*/
        },
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "Bearer token authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
