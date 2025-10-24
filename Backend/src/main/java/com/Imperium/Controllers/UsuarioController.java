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

        // Criptografa a senha antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok(Map.of(
            "status", "sucesso",
            "mensagem", "Usuário registrado com sucesso!"
        ));
    }
    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
    var usuarios = usuarioRepository.findAll();
    return ResponseEntity.ok(usuarios);
}

}

