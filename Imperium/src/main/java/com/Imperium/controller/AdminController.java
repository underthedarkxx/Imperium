package com.Imperium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.dto.UsuarioCriacaoDTO;
import com.Imperium.dto.UsuarioUpdateDTO;
import com.Imperium.model.Usuario;
import com.Imperium.repository.UsuarioRepository;
import com.Imperium.service.UsuarioService;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminController {
    
    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Autowired
    private UsuarioService UsuarioService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR_PRINCIPAL')")
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioCriacaoDTO dto){
        UsuarioService.criarNovoUsuario(dto);
        return ResponseEntity.status(201).body("Usuário criado com sucesso.");
    }
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRATOR_PRINCIPAL', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR_PRINCIPAL')")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateDTO dto) {
        UsuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok("Usuário atualizado com sucesso.");
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR_PRINCIPAL')")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        // Apenas mude o nome do método do serviço que é chamado
        UsuarioService.desativarUsuario(id); 
        return ResponseEntity.ok("Usuário desativado com sucesso.");
    }
}
