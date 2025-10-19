package com.Imperium.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Imperium.dto.UsuarioCriacaoDTO;
import com.Imperium.dto.UsuarioUpdateDTO;
import com.Imperium.model.Funcoes;
import com.Imperium.model.Usuario;
import com.Imperium.repository.FuncoesRepository;
import com.Imperium.repository.UsuarioRepository;


@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FuncoesRepository funcoesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void criarNovoUsuario(UsuarioCriacaoDTO dto){
        Funcoes funcao = funcoesRepository.findById(dto.getFuncaoId())
                .orElseThrow(() -> new RuntimeException("Função não encontrada!"));

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dto.getLogin());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setFuncao(funcao);

        usuarioRepository.save(novoUsuario);
    }

    @Transactional
    public void atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (dto.getFuncaoId() != null) {
            Funcoes novaFuncao = funcoesRepository.findById(dto.getFuncaoId())
                    .orElseThrow(() -> new RuntimeException("Função não encontrada com o ID: " + dto.getFuncaoId()));
            usuario.setFuncao(novaFuncao);
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desativarUsuario(Long id){
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}
