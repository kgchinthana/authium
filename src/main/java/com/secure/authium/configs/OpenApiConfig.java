package com.secure.authium.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * <p>
 * This configuration sets up Swagger UI with proper API documentation,
 * security schemes, and server information.
 * </p>
 *
 * @author Authium Team
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Authium}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Configures the OpenAPI specification.
     *
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme()));
    }

    /**
     * Creates the API information.
     *
     * @return Info object with API details
     */
    private Info apiInfo() {
        return new Info()
                .title(applicationName + " API")
                .description(
                        """
                                **Authium** is an Advanced Enterprise IAM System for Secure Access Control.

                                ## Features
                                - **User Authentication**: Register, login, and manage user sessions
                                - **OAuth Integration**: Support for GitHub, Google, and Facebook OAuth
                                - **JWT Token Management**: Access and refresh token handling
                                - **Password Management**: Reset password functionality
                                - **Role-Based Access Control**: Fine-grained permission management

                                ## Authentication
                                This API uses JWT Bearer token authentication. Include the token in the Authorization header:
                                ```
                                Authorization: Bearer <your-token>
                                ```

                                ## API Endpoints
                                - **/api/authenticate/auth/register** - User registration
                                - **/api/authenticate/auth/login** - User login
                                - **/api/authenticate/auth/oauth** - OAuth authentication
                                - **/api/authenticate/user/refresh** - Refresh access token
                                - **/api/authenticate/auth/reset-request** - Request password reset
                                - **/api/authenticate/auth/reset-password** - Reset password
                                """)
                .version("1.0.0")
                .contact(contact())
                .license(license());
    }

    /**
     * Creates contact information.
     *
     * @return Contact object
     */
    private Contact contact() {
        return new Contact()
                .name("Authium Team")
                .email("kavindugchinthana@gmail.com")
                .url("https://github.com/authium");
    }

    /**
     * Creates license information.
     *
     * @return License object
     */
    private License license() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * Creates server configurations.
     *
     * @return List of servers
     */
    private List<Server> servers() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Server dockerServer = new Server()
                .url("http://localhost:8080")
                .description("Docker Environment");

        return List.of(localServer, dockerServer);
    }

    /**
     * Creates the JWT Bearer security scheme.
     *
     * @return SecurityScheme for JWT
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter your JWT token obtained from the login endpoint");
    }
}
