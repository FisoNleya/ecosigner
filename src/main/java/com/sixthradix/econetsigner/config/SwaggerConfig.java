package com.sixthradix.econetsigner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String CONTACT_EMAIL = "sixthradix.com";

    private static final String AUTHOR_ORG = "Sixth Radix";

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI().info(getInfo());
        return openApi;
    }

    private Info getInfo() {
        return new Info()
                .title("Bill Signer")
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
                .url("https//www.sixth.com/license");
    }




}
