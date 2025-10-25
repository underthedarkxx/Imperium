package com.Imperium.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.DTOs.UsuarioResponseDTO;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para registrar novo usuário
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario novoUsuario) {

        // Verifica se já existe usuário com mesmo login
        if (usuarioRepository.findByLogin(novoUsuario.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "erro",
                "mensagem", "Já existe um usuário com esse login."
            ));
        }

        if (usuarioRepository.findByNomeUsuario(novoUsuario.getNomeUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "erro",
                "mensagem", "Já existe um usuário com esse nome de usuario."
            ));
        }

        if(novoUsuario.getSetorUsuario() == null){
            return ResponseEntity.badRequest().body(Map.of(
                "status", "erro","mensagem", "O campo 'setorUsuario' é obrigatório (ex: 'Gerente', 'Colaborador', etc)."
            ));
        }

        novoUsuario.setId(null); // evita sobrescrever
        novoUsuario.setAtivo(true); // redundante, mas seguro
        novoUsuario.setDataUltimoAcesso(null);

        // Criptografa a senha antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok(Map.of(
            "status", "sucesso",
            "mensagem", "Usuário registrado com sucesso!"
        ));
    }
    // --- LISTAR USUÁRIOS ---
    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
    var usuarios = usuarioRepository.findAll()
        .stream()
        .map(u -> new UsuarioResponseDTO(
            u.getId(),
            u.getNomeUsuario(),
            u.getLogin(),
            u.isAtivo(),
            u.getDataCadastro(),
            u.getDataUltimoAcesso(),
            u.getSetorUsuario()
        ))
        .toList();

    return ResponseEntity.ok(usuarios);
    }
}

