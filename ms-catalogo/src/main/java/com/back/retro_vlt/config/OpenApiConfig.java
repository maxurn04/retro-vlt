package com.back.retro_vlt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configuracionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catálogo y Tienda Retro Vault")
                        .version("1.0.0")
                        .description("Microservicio de Catálogo de Productos, Categorías y Blog para la tienda pública Retro Vault.")
                        .contact(new Contact()
                                .name("Duoc UC - DSY1104")
                                .email("soporte@retrovlt.cl")));
    }
}
