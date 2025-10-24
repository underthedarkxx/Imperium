package com.Imperium; // define o pacote principal da aplicação

import org.springframework.boot.SpringApplication; // classe principal para inicializar Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // habilita configuração automática do Spring Boot

@SpringBootApplication // indica que é a classe principal do Spring Boot
public class ImperiumApplication {

    // Método principal que inicia a aplicação Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(ImperiumApplication.class, args); // executa a aplicação
    }
}
