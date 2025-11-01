package com.Imperium.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Imperium.DTOs.RegistroDTO;
import com.Imperium.DTOs.UsuarioCriacaoDTO;
import com.Imperium.DTOs.UsuarioResponseDTO;
import com.Imperium.DTOs.UsuarioUpdateDTO;
import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Enum.papelUsuario;
import com.Imperium.Models.Setor;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.SetorRepository;
import com.Imperium.Repositorys.UsuarioRepository;

import jakarta.transaction.Transactional;


@Service
public class UsuarioService {
    
    @Autowired
    private SetorRepository setorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void criarNovoUsuario(RegistroDTO dto){
        if(usuarioRepository.findByEmailUsuario(dto.emailUsuario()).isPresent()){
            throw new RuntimeException("Já existe um usuário com esse Email!");
        }

        Setor setor = null;

        if (dto.idSetor() != null) {
            setor = setorRepository.findById(dto.idSetor())
                .orElseThrow(() -> new RuntimeException("Setor com ID " + dto.idSetor() + " não foi encontrado."));
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmailUsuario(dto.emailUsuario());
        novoUsuario.setSenhaUsuario(passwordEncoder.encode(dto.senhaUsuario()));
        novoUsuario.setSetor(setor);

        novoUsuario.setPapelUsuario(papelUsuario.Colaborador);

        usuarioRepository.save(novoUsuario);
    }

    public void criarNovoUsuario(UsuarioCriacaoDTO dto) {
        // Verifica se o e-mail já existe
        if (usuarioRepository.findByEmailUsuario(dto.emailUsuario()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        // Busca o Setor pelo ID fornecido no DTO
        // É importante verificar se o setor existe antes de usá-lo
        Setor setor = null; // Inicia como nulo
        if (dto.idSetor() != null) {
            setor = setorRepository.findById(dto.idSetor())
                .orElseThrow(() -> new RuntimeException("Setor com ID " + dto.idSetor() + " não encontrado"));
        }

        // Cria a nova entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmailUsuario(dto.emailUsuario());
        novoUsuario.setSenhaUsuario(passwordEncoder.encode(dto.senha()));
        novoUsuario.setSetor(setor);

        
        novoUsuario.setPapelUsuario(papelUsuario.Colaborador);
        novoUsuario.setStatusUsuario(StatusUsuario.Ativo);
        novoUsuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(novoUsuario);
    }
    
    @Transactional
    public void atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id)); 

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenhaUsuario(passwordEncoder.encode(dto.senha()));
        }

        if (dto.idSetor() != null) {
            Setor novoSetor = setorRepository.findById(dto.idSetor())
                .orElseThrow(() -> new RuntimeException("Setor com ID " + dto.idSetor() + " não encontrado."));
            usuario.setSetor(novoSetor);
        }
        
        if (dto.papelUsuario() != null) {
            usuario.setPapelUsuario(dto.papelUsuario());
        }
        if (dto.statusUsuario() != null) {
            usuario.setStatusUsuario(dto.statusUsuario());
        }

        usuarioRepository.save(usuario);
    }

    public long contarUsuarios() {
        return usuarioRepository.count();
    }


    public List<UsuarioResponseDTO> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        return usuarios.stream()
                .map(UsuarioResponseDTO::new) // Assumindo que seu DTO tem um construtor que aceita (Usuario u)
                .toList();
    }

    public void apagarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Erro: Usuário com ID " + id + " não encontrado.");
        }
        
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro: Não é possível apagar o usuário. Ele está associado a outros registros.");
        }
    }
}