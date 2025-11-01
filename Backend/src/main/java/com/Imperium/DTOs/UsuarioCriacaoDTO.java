package com.Imperium.DTOs; // define o pacote da classe


public record UsuarioCriacaoDTO(
    String emailUsuario,
    String senha,
    Integer idSetor
) {}