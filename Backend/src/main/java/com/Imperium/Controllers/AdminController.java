package com.Imperium.Controllers; // define o pacote da classe

import java.util.List; // para trabalhar com listas de usuários
import java.util.Map; // permite injeção automática de dependências

import org.springframework.beans.factory.annotation.Autowired; // para retornar respostas HTTP com status e corpo
import org.springframework.http.ResponseEntity; // permite definir permissões por método
import org.springframework.security.access.prepost.PreAuthorize; // mapeia requisições GET
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping; // extrai variáveis da URL
import org.springframework.web.bind.annotation.PathVariable; // mapeia requisições POST
import org.springframework.web.bind.annotation.PostMapping; // mapeia requisições PUT
import org.springframework.web.bind.annotation.PutMapping; // indica que o corpo da requisição será mapeado para um objeto
import org.springframework.web.bind.annotation.RequestBody; // define o caminho base da API
import org.springframework.web.bind.annotation.RequestMapping; // indica que é um controller REST
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.DTOs.UsuarioCriacaoDTO;
import com.Imperium.DTOs.UsuarioResponseDTO;
import com.Imperium.DTOs.UsuarioUpdateDTO;
import com.Imperium.Services.UsuarioService;

@RestController // marca a classe como um controller REST
@RequestMapping("/api/admin/usuarios") // define o caminho base para todos os endpoints desta classe
public class AdminController {
    
    @Autowired // injeta automaticamente o serviço de usuários
    private UsuarioService usuarioService;

    @PostMapping // mapeia requisições POST para criar usuário
    @PreAuthorize("hasAuthority('ROLE_CEO')") // apenas usuários com esta role podem acessar
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioCriacaoDTO dto){ // mapeia o corpo da requisição para DTO
        usuarioService.criarNovoUsuario(dto); // chama o serviço para criar o usuário
        return ResponseEntity.status(201).body("Usuário criado com sucesso."); // retorna status 201 Created
    }
    
    @GetMapping // mapeia requisições GET para listar usuários
    @PreAuthorize("hasAnyAuthority('ROLE_CEO', 'ROLE_ADMINISTRADOR')") 
    // Mude o tipo de retorno para o seu DTO
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        
        List<UsuarioResponseDTO> usuariosDTO = usuarioService.listarTodosUsuarios(); 
        
        return ResponseEntity.ok(usuariosDTO); 
    }
    
    @PutMapping("/{id}") // mapeia requisições PUT para atualizar um usuário pelo ID
    @PreAuthorize("hasAuthority('ROLE_CEO')") // apenas role principal pode atualizar
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateDTO dto) { // extrai id da URL e corpo da requisição
        usuarioService.atualizarUsuario(id, dto); // chama serviço para atualizar usuário
        return ResponseEntity.ok("Usuário atualizado com sucesso."); // retorna status 200 OK
    }

    @GetMapping("/contar") // Mapeia para GET /api/admin/usuarios/contar
    @PreAuthorize("hasAnyAuthority('ROLE_CEO', 'ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> contarUsuarios() {
        long total = usuarioService.contarUsuarios();
        // Retorna um JSON simples: { "totalUsuarios": 5 }
        return ResponseEntity.ok(Map.of("totalUsuarios", total));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CEO')")
    public ResponseEntity<?> apagarUsuario(@PathVariable Long id) {
        try {
            usuarioService.apagarUsuario(id);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário apagado com sucesso."));
        
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "erro",
                "mensagem", e.getMessage()
            ));
        }
    }
}