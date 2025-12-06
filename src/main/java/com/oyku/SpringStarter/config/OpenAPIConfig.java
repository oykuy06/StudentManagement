package com.oyku.SpringStarter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("SpringStarter API")
                        .version("1.0")
                        .description("API documentation for SpringStarter project")
                        .contact(new Contact()
                                .name("Oyku Yetgin")
                                .email("oyetgin09@gmail.com")
                                .url("https://example.com")
                        )
                );
    }
}
