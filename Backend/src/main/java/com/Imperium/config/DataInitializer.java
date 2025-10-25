package com.Imperium.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Imperium.Enum.Setor;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.UsuarioRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>> [DataInitializer] Verificando/Inserindo dados iniciais...");

        // --- CRIA O USUÁRIO ADMIN SE NÃO EXISTIR ---
        if (usuarioRepository.findByLogin("Admin").isEmpty()) {
            Usuario adminUser = new Usuario();
            adminUser.setNomeUsuario("Administrador Principal");
            adminUser.setLogin("Admin");
            adminUser.setSenha(passwordEncoder.encode("Admin123*"));
            adminUser.setSetorUsuario(Setor.CEO); // ou Setor.Administrador
            adminUser.setAtivo(true);

            usuarioRepository.save(adminUser);
            System.out.println(">>> [DataInitializer] Usuário 'Admin' criado com sucesso.");
        } else {
            System.out.println(">>> [DataInitializer] Usuário 'Admin' já existe.");
        }

        System.out.println(">>> [DataInitializer] Dados iniciais verificados/Inseridos.");
    }
}
