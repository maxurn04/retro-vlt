package com.back.retro_vlt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST Tienda Retro-VLT")
                        .version("1.0.0")
                        .description("API RESTful para la tienda online y panel de administración Retro-VLT (Evaluación DSY1104 Duoc UC).")
                        .contact(new Contact()
                                .name("Duoc UC - DSY1104")
                                .email("soporte@retrovlt.cl")));
    }
}
