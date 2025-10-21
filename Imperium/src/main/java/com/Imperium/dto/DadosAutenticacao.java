package com.Imperium.dto; // define o pacote da classe

// Record para receber os dados de login do corpo da requisição
public record DadosAutenticacao(String login, String senha) {
    // Record gera automaticamente construtor, getters e equals/hashCode
}