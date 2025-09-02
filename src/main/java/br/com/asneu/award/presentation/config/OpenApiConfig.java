package br.com.asneu.award.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:8080").description("Servidor Local")))
                .info(new Info()
                        .title("Golden Raspberry Awards API")
                        .version("1.0.0")
                        .description("API RESTful para consulta dos indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards. " +
                                    "Esta API permite obter informações sobre os intervalos entre prêmios consecutivos dos produtores.")
                        .contact(new Contact()
                                .name("SN Sistemas")
                                .email("andresnjr@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}