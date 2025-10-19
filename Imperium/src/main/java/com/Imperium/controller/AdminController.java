package com.Imperium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.dto.UsuarioCriacaoDTO;
import com.Imperium.service.UsuarioService;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminController {
    
    @Autowired
    private UsuarioService UsuarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR_PRINCIPAL')")
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioCriacaoDTO dto){
        UsuarioService.criarNovoUsuario(dto);
        return ResponseEntity.status(201).body("Usuário criado com sucesso.");
    }
    
}
