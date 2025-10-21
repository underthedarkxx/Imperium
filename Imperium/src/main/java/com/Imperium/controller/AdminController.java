package com.Imperium.controller; // define o pacote da classe

import java.util.List; // para trabalhar com listas de usuários

import org.springframework.beans.factory.annotation.Autowired; // permite injeção automática de dependências
import org.springframework.http.ResponseEntity; // para retornar respostas HTTP com status e corpo
import org.springframework.security.access.prepost.PreAuthorize; // permite definir permissões por método
import org.springframework.web.bind.annotation.DeleteMapping; // mapeia requisições DELETE
import org.springframework.web.bind.annotation.GetMapping; // mapeia requisições GET
import org.springframework.web.bind.annotation.PathVariable; // extrai variáveis da URL
import org.springframework.web.bind.annotation.PostMapping; // mapeia requisições POST
import org.springframework.web.bind.annotation.PutMapping; // mapeia requisições PUT
import org.springframework.web.bind.annotation.RequestBody; // indica que o corpo da requisição será mapeado para um objeto
import org.springframework.web.bind.annotation.RequestMapping; // define o caminho base da API
import org.springframework.web.bind.annotation.RestController; // indica que é um controller REST

import com.Imperium.dto.UsuarioCriacaoDTO; // DTO para criação de usuários
import com.Imperium.dto.UsuarioUpdateDTO; // DTO para atualização de usuários
import com.Imperium.model.Usuario; // modelo de usuário
import com.Imperium.repository.UsuarioRepository; // repositório para acessar usuários no banco
import com.Imperium.service.UsuarioService; // serviço de usuários com regras de negócio

@RestController // marca a classe como um controller REST
@RequestMapping("/api/admin/usuarios") // define o caminho base para todos os endpoints desta classe
public class AdminController {
    
    private final UsuarioRepository usuarioRepository; // repositório de usuários

    // Construtor para injetar o repositório
    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Autowired // injeta automaticamente o serviço de usuários
    private UsuarioService UsuarioService;

    @PostMapping // mapeia requisições POST para criar usuário
    @PreAuthorize("hasAuthority('ADMINISTRADOR_PRINCIPAL')") // apenas usuários com esta role podem acessar
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioCriacaoDTO dto){ // mapeia o corpo da requisição para DTO
        UsuarioService.criarNovoUsuario(dto); // chama o serviço para criar o usuário
        return ResponseEntity.status(201).body("Usuário criado com sucesso."); // retorna status 201 Created
    }
    
    @GetMapping // mapeia requisições GET para listar usuários
    @PreAuthorize("hasAnyAuthority('ROLE_ADMINISTRATOR_PRINCIPAL', 'ROLE_ADMINISTRADOR')") // permite múltiplas roles
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll(); // busca todos os usuários
        return ResponseEntity.ok(usuarios); // retorna status 200 OK com a lista
    }
    
    @PutMapping("/{id}") // mapeia requisições PUT para atualizar um usuário pelo ID
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR_PRINCIPAL')") // apenas role principal pode atualizar
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateDTO dto) { // extrai id da URL e corpo da requisição
        UsuarioService.atualizarUsuario(id, dto); // chama serviço para atualizar usuário
        return ResponseEntity.ok("Usuário atualizado com sucesso."); // retorna status 200 OK
    }

    @DeleteMapping("/{id}") // mapeia requisições DELETE para desativar usuário pelo ID
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR_PRINCIPAL')") // apenas role principal pode deletar
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        UsuarioService.desativarUsuario(id); // chama serviço para desativar o usuário
        return ResponseEntity.ok("Usuário desativado com sucesso."); // retorna status 200 OK
    }
}