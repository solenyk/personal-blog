package com.kopchak.authserver.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Personal blog authorization server",
                version = "1.0.0",
                description = "This authorization server provides an OAuth2 authorization mechanism, methods for " +
                        "authenticating users, issuing and refreshing access tokens, and registration endpoint. ",
                contact = @Contact(
                        name = "Iryna Kopchak",
                        email = "iryna.kopchak39@gmail.com",
                        url = "https://www.linkedin.com/in/iryna-kopchak/"
                ),
                license = @License(name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local development server")
        },
        externalDocs = @ExternalDocumentation(description = "Instructions for how to run and use this project",
                url = "https://github.com/solenyk/personal-blog/blob/develop/README.md"
        )
)
public class OpenApi30Config {
}
