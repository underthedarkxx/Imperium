package com.Imperium.service; // define o pacote da classe

import org.springframework.beans.factory.annotation.Autowired; // para injeção automática de dependências
import org.springframework.security.core.userdetails.UserDetails; // interface que representa o usuário autenticável
import org.springframework.security.core.userdetails.UserDetailsService; // interface do Spring Security para carregar usuários
import org.springframework.security.core.userdetails.UsernameNotFoundException; // exceção quando usuário não é encontrado
import org.springframework.stereotype.Service; // marca a classe como um serviço Spring

import com.Imperium.repository.UsuarioRepository; // repositório para acessar usuários

@Service // indica que é um serviço Spring
public class AuthenticationService implements UserDetailsService { // implementa UserDetailsService para autenticação

    @Autowired // injeta automaticamente o repositório de usuários
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca usuário pelo login; se não encontrado, lança exceção
        return repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Dados inválidos."));
    }
}