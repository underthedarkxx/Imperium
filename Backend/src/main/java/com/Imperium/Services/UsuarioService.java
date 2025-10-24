package com.Imperium.Services; // define o pacote da classe

import org.springframework.beans.factory.annotation.Autowired; // para injeção de dependências
import org.springframework.security.crypto.password.PasswordEncoder; // para criptografar senhas
import org.springframework.stereotype.Service; // marca como serviço Spring
import org.springframework.transaction.annotation.Transactional; // marca métodos como transacionais

import com.Imperium.DTOs.UsuarioCriacaoDTO;
import com.Imperium.DTOs.UsuarioUpdateDTO;
import com.Imperium.Models.Funcoes;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.FuncoesRepository;
import com.Imperium.Repositorys.UsuarioRepository;

@Service // marca como serviço Spring
public class UsuarioService {
    
    @Autowired // injeta automaticamente o repositório de usuários
    private UsuarioRepository usuarioRepository;

    @Autowired // injeta automaticamente o repositório de funções
    private FuncoesRepository funcoesRepository;

    @Autowired // injeta o encoder de senhas
    private PasswordEncoder passwordEncoder;

    // Método para criar um novo usuário
    public void criarNovoUsuario(UsuarioCriacaoDTO dto){
        Funcoes funcao = funcoesRepository.findById(dto.getFuncaoId())
                .orElseThrow(() -> new RuntimeException("Função não encontrada!")); // busca função ou lança exceção

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dto.getLogin()); // define login
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha())); // criptografa e define senha
        novoUsuario.setFuncao(funcao); // define função

        usuarioRepository.save(novoUsuario); // salva usuário no banco
    }

    // Método transacional para atualizar usuário existente
    @Transactional
    public void atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id)); // busca usuário

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha())); // atualiza senha se fornecida
        }

        if (dto.getFuncaoId() != null) {
            Funcoes novaFuncao = funcoesRepository.findById(dto.getFuncaoId())
                    .orElseThrow(() -> new RuntimeException("Função não encontrada com o ID: " + dto.getFuncaoId())); // busca nova função
            usuario.setFuncao(novaFuncao); // atualiza função
        }

        usuarioRepository.save(usuario); // salva alterações
    }

    // Método transacional para desativar usuário
    @Transactional
    public void desativarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id)); // busca usuário

        usuario.setAtivo(false); // marca usuário como inativo

        usuarioRepository.save(usuario); // salva alteração
    }
}