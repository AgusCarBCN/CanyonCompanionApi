package com.canyoncompanion.canyon_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "E-Commerce API",
                version = "1.0",
                description = """
                        E-Commerce API – Manage users, orders, and payments.
                        Features:
                        - User Management: Create, update, and deactivate users. Assign roles and manage addresses.
                        - Order Management: Create, track, and cancel orders. Manage order products and quantities.
                        - Payment Processing: Create payments. Refund and confirm refunds for successful payments.
                        - Security & Authentication: JWT-based authentication. Role-based access control (USER, ADMIN).
                        - Platform Ready: Fully RESTful API. Supports secure e-commerce operations.
                        """,
                contact = @Contact(
                        name = "Agustin Carnerero",
                        email = "${EMAIL}"
                )
        ),
        servers = {
                @Server(
                        description = "Local environment",
                        url = "http://localhost:8081"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}