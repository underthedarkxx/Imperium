package com.Imperium.dto;

// Record para receber os dados de login do corpo da requisição
public record DadosAutenticacao(String login, String senha) {
}