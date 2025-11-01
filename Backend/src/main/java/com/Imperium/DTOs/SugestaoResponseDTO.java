package com.Imperium.DTOs;

import java.time.LocalDateTime;
import com.Imperium.Enum.statusSugestao;
import com.Imperium.Models.Sugestoes; // Importe a entidade

public record SugestaoResponseDTO(
    Long id,
    String titulo,
    String descricaoSugestao,
    LocalDateTime dataEnvioSugestao,
    statusSugestao statusSugestao,
    String emailUsuario, // Para saber QUEM enviou
    Integer idSetor      // Para saber de QUAL SETOR veio
) {
    /**
     * Construtor de conveniência que converte a Entidade no DTO.
     */
    public SugestaoResponseDTO(Sugestoes sugestao) {
        this(
            sugestao.getId(),
            sugestao.getTitulo(),
            sugestao.getDescricaoSugestoes(),
            sugestao.getDataEnvioSugestao(),
            sugestao.getStatusSugestao(),
            // Tratamento para evitar NullPointer
            sugestao.getUsuario() != null ? sugestao.getUsuario().getEmailUsuario() : null,
            sugestao.getSetor() != null ? sugestao.getSetor().getIdSetor() : null
        );
    }
}
