package com.Imperium.config; // Verifique se este é o pacote correto da sua classe

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Imperium.model.Funcoes;
import com.Imperium.model.Usuario;
import com.Imperium.repository.FuncoesRepository;
import com.Imperium.repository.UsuarioRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    private final FuncoesRepository funcoesRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // O Spring vai injetar automaticamente as dependências que você precisa
    public DataInitializer(FuncoesRepository funcoesRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.funcoesRepository = funcoesRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>> [DataInitializer] INICIANDO A VERIFICAÇÃO/INSERÇÃO DE DADOS INICIAIS...");

        // --- CRIA AS FUNÇÕES (ROLES) ---
        // Busca a role, e se não encontrar, cria uma nova.
        Funcoes adminPrincipalRole = funcoesRepository.findByNome("ADMINISTRADOR_PRINCIPAL").orElseGet(() -> {
            Funcoes newRole = new Funcoes();
            newRole.setNome("ADMINISTRADOR_PRINCIPAL");
            newRole.setDescricao("Pode gerenciar administradores e usuários.");
            System.out.println(">>> [DataInitializer] Criando role: ADMINISTRADOR_PRINCIPAL");
            return funcoesRepository.save(newRole);
        });

        Funcoes adminRole = funcoesRepository.findByNome("ADMINISTRADOR").orElseGet(() -> {
            Funcoes newRole = new Funcoes();
            newRole.setNome("ADMINISTRADOR");
            newRole.setDescricao("Pode gerenciar apenas usuários padrão.");
            System.out.println(">>> [DataInitializer] Criando role: ADMINISTRADOR");
            return funcoesRepository.save(newRole);
        });

        funcoesRepository.findByNome("USUARIO_PADRAO").orElseGet(() -> {
            Funcoes newRole = new Funcoes();
            newRole.setNome("USUARIO_PADRAO");
            newRole.setDescricao("Acesso básico ao sistema.");
            System.out.println(">>> [DataInitializer] Criando role: USUARIO_PADRAO");
            return funcoesRepository.save(newRole);
        });


        // --- CRIA O USUÁRIO ADMIN ---
        // Verifica se o usuário 'Admin' já existe antes de criar
        if (usuarioRepository.findByLogin("Admin").isEmpty()) {
            Usuario adminUser = new Usuario();
            adminUser.setLogin("Admin");
            // CRIPTOGRAFA A SENHA ANTES DE SALVAR!
            adminUser.setSenha(passwordEncoder.encode("Admin123*"));
            adminUser.setFuncao(adminPrincipalRole);
            
            usuarioRepository.save(adminUser);
            System.out.println(">>> [DataInitializer] Usuário 'Admin' criado com sucesso.");
        } else {
            System.out.println(">>> [DataInitializer] Usuário 'Admin' já existe.");
        }

        System.out.println(">>> [DataInitializer] DADOS INICIAIS VERIFICADOS/INSERIDOS. <<<");
    }
}
