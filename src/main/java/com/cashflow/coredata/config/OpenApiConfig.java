package com.cashflow.coredata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.title}")
    private String applicationTitle;

    @Value("${application.description}")
    private String applicationDescription;

    @Value("${application.contact.name}")
    private String applicationContactName;

    @Value("${application.contact.email}")
    private String applicationContactEmail;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(info());
    }

    private Info info() {
        return new Info()
                .title(applicationTitle)
                .description(applicationDescription)
                .version(applicationVersion)
                .contact(contact())
                .license(license());
    }

    private Contact contact() {
        return new Contact()
                .name(applicationContactName)
                .email(applicationContactEmail);
    }

    private License license() {
        return new License()
                .name("MIT License")
                .url("http://www.opensource.org/licenses/mit-license.php");
    }}
