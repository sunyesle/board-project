package com.sunyesle.board_project.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name(securitySchemeName);

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, securityScheme)
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .info(new Info()
                        .title("Board Project API")
                );
    }
}
