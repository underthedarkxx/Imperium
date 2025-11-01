package com.Imperium.DTOs;

import java.time.LocalDateTime;

import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Enum.papelUsuario;

public record UsuarioResponseDTO(
    Long id,
    String emailUsuario,
    papelUsuario papelUsuario,
    LocalDateTime dataCadastro,
    LocalDateTime dataUltimoAcesso,
    Integer idSetor,
    StatusUsuario status
) {}