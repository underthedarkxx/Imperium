package com.Imperium.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.DTOs.SugestaoCriacaoDTO;
import com.Imperium.Models.Usuario; // Sua entidade Usuário (que implementa UserDetails)
import com.Imperium.Services.SugestaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sugestoes") // Nova rota base
public class SugestaoController {

    @Autowired
    private SugestaoService sugestaoService;

    @PostMapping
    public ResponseEntity<?> criarSugestao(
        @Valid @RequestBody SugestaoCriacaoDTO dto,
        @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        
        // @AuthenticationPrincipal injeta o usuário que fez o login
        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body(Map.of("erro", "Usuário não autenticado."));
        }

        try {
            sugestaoService.criarSugestao(dto, usuarioLogado);
            return ResponseEntity.status(201).body(Map.of("mensagem", "Sugestão enviada com sucesso."));
        
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
