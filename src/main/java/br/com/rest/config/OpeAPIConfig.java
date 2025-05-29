package br.com.rest.config;

import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpeAPIConfig {

    @Bean
    OpenAPI costomOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Hello Swagger OpenAPI")
                                .version("v1")
                                .description("Some description about Swagger OpenAPI")
                                .termsOfService("")
                                .license(new License()
                                        .name("Apache 2.0")
                                        .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }

}
