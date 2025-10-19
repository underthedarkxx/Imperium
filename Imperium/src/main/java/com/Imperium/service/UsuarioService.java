package com.Imperium.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Imperium.dto.UsuarioCriacaoDTO;
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
}
