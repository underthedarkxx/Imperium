package com.Imperium.DTOs;

import java.time.LocalDateTime;

import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Enum.papelUsuario;
import com.Imperium.Models.Usuario; // IMPORTE A ENTIDADE

public record UsuarioResponseDTO(
    Long id,
    String emailUsuario,
    papelUsuario papelUsuario,
    LocalDateTime dataCadastro,
    LocalDateTime dataUltimoAcesso,
    Integer idSetor,
    StatusUsuario status
) {
    
    public UsuarioResponseDTO(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getEmailUsuario(),
            usuario.getPapelUsuario(),
            usuario.getDataCadastro(),
            usuario.getDataUltimoAcesso(),
            // Lógica para evitar NullPointerException se o setor for nulo
            usuario.getSetor() != null ? usuario.getSetor().getIdSetor() : null,
            usuario.getStatusUsuario()
        );
    }
}