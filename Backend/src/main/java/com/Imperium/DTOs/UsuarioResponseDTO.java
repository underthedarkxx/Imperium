package com.Imperium.DTOs;

import java.time.LocalDateTime;

import com.Imperium.Enum.Setor;

public record UsuarioResponseDTO(
    Long id,
    String nomeUsuario,
    String login,
    boolean ativo,
    LocalDateTime dataCadastro,
    LocalDateTime dataUltimoAcesso,
    Setor setorUsuario
) {}