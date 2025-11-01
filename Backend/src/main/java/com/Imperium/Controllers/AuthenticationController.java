package com.Imperium.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails; // Importe
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.DTOs.LoginRequestDTO;
import com.Imperium.Services.JwtService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // 2. Injete o JwtService
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.emailUsuario(), loginRequest.senha())
            );

            // 3. NÃO crie a sessão (remova SecurityContextHolder)
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 4. GERE o token
            // O getPrincipal() aqui é o seu objeto 'Usuario' que implementa 'UserDetails'
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            // 5. Retorne o token no corpo da resposta
            return ResponseEntity.ok(Map.of(
                "status", "sucesso",
                "mensagem", "Login bem-sucedido!",
                "token", token // <-- Retorna o token
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "status", "erro",
                "mensagem", "Credenciais inválidas. Verifique seu e-mail e senha."
            ));
        }
    }
}
