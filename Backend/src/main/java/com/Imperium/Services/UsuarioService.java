package com.Imperium.Services; // define o pacote da classe

import org.springframework.beans.factory.annotation.Autowired; // para injeção de dependências
import org.springframework.security.crypto.password.PasswordEncoder; // para criptografar senhas
import org.springframework.stereotype.Service; // marca como serviço Spring
import org.springframework.transaction.annotation.Transactional; // marca métodos como transacionais

import com.Imperium.DTOs.UsuarioCriacaoDTO;
import com.Imperium.DTOs.UsuarioUpdateDTO;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.UsuarioRepository;

@Service // marca como serviço Spring
public class UsuarioService {
    
    @Autowired // injeta automaticamente o repositório de usuários
    private UsuarioRepository usuarioRepository;

    @Autowired // injeta o encoder de senhas
    private PasswordEncoder passwordEncoder;

    // Método para criar um novo usuário
    public void criarNovoUsuario(UsuarioCriacaoDTO dto){
        if(usuarioRepository.findByLogin(dto.getLogin()).isPresent()){
            throw new RuntimeException("Já existe um usuário com esse login!");
        }
        if(usuarioRepository.findByNomeUsuario(dto.getNomeUsuario()).isPresent()){
            throw new RuntimeException("Já existe um usuário com esse nome de usuário!");
        }
        if(dto.getSetorUsuario() == null){
            throw new RuntimeException("O campo 'setorUsuario' é obrigatório!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setAtivo(true);
        novoUsuario.setNomeUsuario(dto.getNomeUsuario());
        novoUsuario.setLogin(dto.getLogin());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setSetorUsuario(dto.getSetorUsuario());

        usuarioRepository.save(novoUsuario);
    }

    // Método transacional para atualizar usuário existente
    @Transactional
    public void atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id)); // busca usuário

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha())); // atualiza senha se fornecida
        }

        if (dto.getSetorUsuario() != null){
            usuario.setSetorUsuario(dto.getSetorUsuario());
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