package com.sixthradix.econetsigner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String CONTACT_EMAIL = "www.sixthradix.com";

    private static final String AUTHOR_ORG = "Sixth Radix";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title("Eco-Signer")
                .contact(getContact())
                .description("")
                .version("1.0.1")
                .license(getLicense());
    }

    private Contact getContact() {
        return new Contact()
                .email(CONTACT_EMAIL)
                .name(AUTHOR_ORG);
    }

    private License getLicense() {
        return new License()
                .name("Proprietary")
                .url("https//www.sixthradix.com/license");
    }




}
