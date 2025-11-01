package com.Imperium.DTOs;

// Você pode usar 'record' (moderno) ou 'class'
public record RegistroDTO(
    String emailUsuario,
    String senhaUsuario,
    Integer idSetor  // Recebe apenas o ID do setor
){}