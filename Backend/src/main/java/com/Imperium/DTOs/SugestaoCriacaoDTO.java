package com.Imperium.DTOs;

import jakarta.validation.constraints.NotBlank;

public record SugestaoCriacaoDTO(
    @NotBlank String titulo,
    @NotBlank String descricaoSugestao
) {}
