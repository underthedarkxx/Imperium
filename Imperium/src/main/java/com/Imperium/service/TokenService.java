package com.Imperium.service; // define o pacote da classe

import java.time.Instant; // para representar instantes de tempo
import java.time.LocalDateTime; // para manipular datas e horas
import java.time.ZoneOffset; // para conversão de fuso horário

import org.springframework.beans.factory.annotation.Value; // para injetar valores de propriedades
import org.springframework.stereotype.Service; // marca como serviço Spring

import com.Imperium.model.Usuario; // modelo de usuário
import com.auth0.jwt.JWT; // biblioteca JWT
import com.auth0.jwt.algorithms.Algorithm; // algoritmo de assinatura JWT
import com.auth0.jwt.exceptions.JWTCreationException; // exceção na criação de token
import com.auth0.jwt.exceptions.JWTVerificationException; // exceção na validação de token

@Service // marca a classe como serviço Spring
public class TokenService {
    
    @Value("${api.security.token.secret}") // injeta a chave secreta do application.properties
    private String secret;

    // Método para gerar token JWT para um usuário
    public String gerarToken(Usuario usuario){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret); // define algoritmo de assinatura
            return JWT.create()
                .withIssuer("API Imperium") // define o emissor do token
                .withSubject(usuario.getLogin()) // define o assunto do token como o login do usuário
                .withExpiresAt(dataExpiracao()) // define data de expiração do token
                .sign(algorithm); // assina o token com o algoritmo
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception); // trata erro na criação
        }
    }

    // Método para validar o token JWT e retornar o assunto (login) se válido
    public String validarToken(String tokenJWT){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // define algoritmo para verificação
            return JWT.require(algorithm)
            .withIssuer("API Imperium") // garante que o token seja do emissor correto
            .build()
            .verify(tokenJWT) // valida o token
            .getSubject(); // retorna o login do usuário
        } catch (JWTVerificationException exception) {
            return null; // retorna null se token inválido
        }
    }

    // Método privado para calcular data de expiração (2 horas à frente)
    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}