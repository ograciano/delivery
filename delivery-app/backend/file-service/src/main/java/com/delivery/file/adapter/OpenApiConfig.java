package com.delivery.file.adapter;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
    // This class is used to configure OpenAPI documentation for the File Service
    // You can add your OpenAPI configuration here if needed
    // For example, you can define API info, servers, security, etc.
    // If you are using SpringDoc, you can use annotations like @OpenAPIDefinition
    // to customize the OpenAPI documentation
    // Example:
    // @OpenAPIDefinition(
    //     info = @Info(
    //         title = "File Service API",
    //         version = "1.0.0",
    //         description = "API for managing files in the delivery system"
    //     ),
    //     servers = {
    //         @Server(url = "http://localhost:8080", description = "Local server"),
    //         @Server(url = "https://api.delivery.com", description = "Production server")
    //     }
    // )

    // You can also define security schemes, tags, and other OpenAPI components
    // For more information, refer to the SpringDoc documentation:
    // https://springdoc.org/
    // https://springdoc.org/v1.5.x/#springdoc-openapi-3
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Service API")
                        .version("1.0.0")
                        .description("API for managing files in the delivery system"));
    }

}
