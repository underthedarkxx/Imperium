package com.Imperium.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.DTOs.RegistroDTO;
import com.Imperium.DTOs.UsuarioResponseDTO;
import com.Imperium.Repositorys.UsuarioRepository;
import com.Imperium.Services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para registrar novo usuário
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO dto) {
        try{
            usuarioService.criarNovoUsuario(dto);

            return ResponseEntity.ok(Map.of(
                "status", "sucesso",
                "mensagem", "Usuario registrado com sucesso!"
            ));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of(
                "status", "erro",
                "mensagem", e.getMessage()
            ));
        }
    }
    // --- LISTAR USUÁRIOS ---
    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios() {
    var usuarios = usuarioRepository.findAll()
        .stream()
        .map(u -> new UsuarioResponseDTO(
            u.getId(),
            u.getEmailUsuario(),
            u.getPapelUsuario(),
            u.getDataCadastro(),
            u.getDataUltimoAcesso(),
            u.getSetor() != null ? u.getSetor().getIdSetor() : null,
            u.getStatusUsuario()
        ))
        .toList();

    return ResponseEntity.ok(usuarios);
    }

    // ... dentro da classe UsuarioController ...

    // ... (seu método /registrar) ...

    // ... (seu método /listar) ...


    // --- CONTAR USUÁRIOS ---
    @GetMapping("/contar")
    public ResponseEntity<?> contarUsuarios() {
        try {
            // O .count() retorna um 'long' com o total de registros na tabela
            long total = usuarioRepository.count(); 
            
            // Retorna um JSON simples: { "totalUsuarios": 5 }
            return ResponseEntity.ok(Map.of("totalUsuarios", total));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "erro",
                "mensagem", "Não foi possível contar os usuários."
            ));
        }
    }
}

