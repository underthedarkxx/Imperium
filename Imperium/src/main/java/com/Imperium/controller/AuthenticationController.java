/*package com.Imperium.controller; // define o pacote da classe

import org.springframework.beans.factory.annotation.Autowired; // permite injeção automática de dependências
import org.springframework.http.ResponseEntity; // para retornar respostas HTTP
import org.springframework.security.authentication.AuthenticationManager; // gerencia autenticação no Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // representa autenticação baseada em username/senha
import org.springframework.web.bind.annotation.PostMapping; // mapeia requisições POST
import org.springframework.web.bind.annotation.RequestBody; // mapeia corpo da requisição para objeto
import org.springframework.web.bind.annotation.RequestMapping; // define caminho base da API
import org.springframework.web.bind.annotation.RestController; // indica que é um controller REST

import com.Imperium.dto.DadosAutenticacao; // DTO com login e senha
import com.Imperium.dto.DadosTokenJWT; // DTO com token JWT
import com.Imperium.model.Usuario; // modelo de usuário
import com.Imperium.service.TokenService; // serviço para gerar tokens JWT

@RestController // marca a classe como REST controller
@RequestMapping("/api/login") // define caminho base para autenticação
public class AuthenticationController {

    @Autowired // injeta automaticamente AuthenticationManager
    private AuthenticationManager manager; // gerenciador de autenticação do Spring

    @Autowired // injeta automaticamente TokenService
    private TokenService tokenService; // serviço para gerar tokens JWT

    @PostMapping // mapeia requisições POST para login
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody DadosAutenticacao dados) { // recebe login e senha
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha()); // cria token de autenticação com login e senha

        var authentication = manager.authenticate(authenticationToken); // autentica o usuário usando AuthenticationManager

        var usuario = (Usuario) authentication.getPrincipal(); // obtém o usuário autenticado

        var tokenJWT = tokenService.gerarToken(usuario); // gera token JWT para o usuário

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT)); // retorna o token JWT na resposta HTTP 200 OK
    }
}*/

package com.Imperium.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.model.Usuario;
import com.Imperium.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        var usuario = usuarioRepository.findByLogin(loginRequest.getLogin());

        if (usuario.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "status", "erro",
                "mensagem", "Usuário não encontrado"
            ));
        }

        var user = usuario.get();
        if (!passwordEncoder.matches(loginRequest.getSenha(), user.getSenha())) {
            return ResponseEntity.status(401).body(Map.of(
                "status", "erro",
                "mensagem", "Senha incorreta"
            ));
        }

        return ResponseEntity.ok(Map.of(
            "status", "sucesso",
            "mensagem", "Login bem-sucedido!"
        ));
    }

}
