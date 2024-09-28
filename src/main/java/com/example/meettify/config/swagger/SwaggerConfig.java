package com.example.meettify.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final String REFERENCE = "Bearer";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)// OpenAPI 3.0
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.backend.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Meettify API 문서")
                .description("주고받을 데이터를 문서화")
                .version("1.0")
                .build();
    }
}


