package com.enigmacamp.shopify.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shopify App",
                version = "1.0",
                description = "Shopify App API",
                contact = @Contact(
                        name = "Enigma Camp",
                        url = "https://enigmacamp.com"
                )
        )
)
public class OpenAPIConfiguration {
}

