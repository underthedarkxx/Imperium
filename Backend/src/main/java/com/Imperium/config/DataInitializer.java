package com.Imperium.config;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Enum.papelUsuario;
import com.Imperium.Models.Setor;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.SetorRepository;
import com.Imperium.Repositorys.UsuarioRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SetorRepository setorRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder,SetorRepository setorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.setorRepository = setorRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>> [DataInitializer] Verificando/Inserindo dados iniciais...");

        String nomeSetorAdmin = "ADMINISTRAÇÃO";

        Setor setorAdmin = setorRepository.findByNomeSetor(nomeSetorAdmin).orElseGet(() -> {
                System.out.println(">>> [DataInitializer] Setor '" + nomeSetorAdmin + "' não encontrado. Criando...");
                Setor novoSetor = new Setor();
                novoSetor.setNomeSetor(nomeSetorAdmin);
                novoSetor.setDescricao("Setor administrativo principal.");
                novoSetor.setRamalSetor("0001");
                return setorRepository.save(novoSetor);
            });

        String emailAdmin = "ceo@imperium.com";

        // --- CRIA O USUÁRIO ADMIN SE NÃO EXISTIR ---
        if (usuarioRepository.findByEmailUsuario(emailAdmin).isEmpty()) {
            Usuario adminUser = new Usuario();
            adminUser.setEmailUsuario(emailAdmin);
            adminUser.setSenhaUsuario(passwordEncoder.encode("Admin123*"));
            adminUser.setPapelUsuario(papelUsuario.CEO);
            adminUser.setSetor(setorAdmin);
            adminUser.setStatusUsuario(StatusUsuario.Ativo);
            adminUser.setDataCadastro(LocalDateTime.now());
            usuarioRepository.save(adminUser);
            System.out.println(">>> [DataInitializer] Usuário 'Admin' criado com sucesso.");
        } else {
            System.out.println(">>> [DataInitializer] Usuário 'Admin' já existe.");
        }

        System.out.println(">>> [DataInitializer] Dados iniciais verificados/Inseridos.");
    }
}
