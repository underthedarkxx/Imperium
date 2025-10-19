package com.Imperium.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.Imperium.model.Funcoes;
import com.Imperium.model.Usuario;
import com.Imperium.repository.FuncoesRepository;
import com.Imperium.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner{

    private final UsuarioRepository usuarioRepository;
    private final FuncoesRepository funcoesRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, FuncoesRepository funcoesRepository ,PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.funcoesRepository = funcoesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception{
        Funcoes adminRole = criarFuncaoSeNaoExistir("ADMINISTRADOR_PRINCIPAL", "Gerencia tudo");
        criarFuncaoSeNaoExistir("ADMINISTRADOR", "Gerencia usuários padrão");
        criarFuncaoSeNaoExistir("Usuario_Padrao", "Acesso Basico");

        if(usuarioRepository.findByLogin("Admin").isEmpty()){
            Usuario adminUsuario = new Usuario();
            adminUsuario.setLogin("Admin");
            adminUsuario.setSenha(passwordEncoder.encode("Admin"));

            adminUsuario.setFuncao(adminRole);

            usuarioRepository.save(adminUsuario);
            System.out.println(">>> Usuário Admin criado com sucesso!");
        }
    }

    private Funcoes criarFuncaoSeNaoExistir(String nome, String descricao){
        return funcoesRepository.findByNome(nome).orElseGet(() -> {
            Funcoes novaFuncao = new Funcoes();
            novaFuncao.setNome(nome);
            novaFuncao.setDescricao(descricao);
            return funcoesRepository.save(novaFuncao);
        });
    }
}
