package cl.wallmart.desafio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.openapi.title:Ventanas de Despacho API}")
    private String title;

    @Value("${app.openapi.description:API REST para consulta y reserva de ventanas de despacho con cobertura geográfica y control de capacidad bajo concurrencia.}")
    private String description;

    @Value("${app.openapi.version:1.0.0}")
    private String version;

    /**
     * Base URL para el server en Swagger UI.
     * En local normalmente puede ir vacío (Springdoc infiere),
     * pero es útil setearlo en ciertos entornos.
     * Ejemplo: http://localhost:8080
     */
    @Value("${app.openapi.server-url:}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact().name("Santiago"));

        OpenAPI api = new OpenAPI().info(info);

        if (serverUrl != null && !serverUrl.isBlank()) {
            api.setServers(List.of(new Server().url(serverUrl)));
        }

        return api;
    }
}
