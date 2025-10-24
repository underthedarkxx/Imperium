package com.Imperium.DTOs; // define o pacote da classe

// Record para enviar o token JWT na resposta
public record DadosTokenJWT(String token) {
    // Record gera automaticamente construtor, getter e equals/hashCode
}