package com.Imperium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.dto.DadosAutenticacao;
import com.Imperium.dto.DadosTokenJWT;
import com.Imperium.model.Usuario;
import com.Imperium.service.TokenService;

@RestController
@RequestMapping("/api/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager; // O gerenciador de autenticação do Spring

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody DadosAutenticacao dados) {
        // 1. Cria um DTO de autenticação com os dados recebidos (login/senha)
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        // 2. O manager usa o seu UserDetailsService para validar o usuário
        var authentication = manager.authenticate(authenticationToken);

        // 3. Se a autenticação for bem-sucedida, pega o objeto do usuário
        var usuario = (Usuario) authentication.getPrincipal();

        // 4. Gera o token JWT com base no usuário autenticado
        var tokenJWT = tokenService.gerarToken(usuario);

        // 5. Retorna o token em um DTO de resposta com status 200 OK
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
