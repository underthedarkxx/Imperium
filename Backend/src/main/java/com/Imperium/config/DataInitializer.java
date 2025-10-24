package com.Imperium.config; // define o pacote da classe

import org.springframework.boot.ApplicationArguments; // argumentos passados na inicialização da aplicação
import org.springframework.boot.ApplicationRunner; // interface para executar código após o Spring Boot iniciar
import org.springframework.security.crypto.password.PasswordEncoder; // usado para criptografar senhas
import org.springframework.stereotype.Component; // marca a classe como componente Spring

import com.Imperium.Models.Funcoes;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.FuncoesRepository;
import com.Imperium.Repositorys.UsuarioRepository;

@Component // registra a classe como componente Spring, será executada automaticamente
public class DataInitializer implements ApplicationRunner { // implementa ApplicationRunner para rodar código após o Spring iniciar

    private final FuncoesRepository funcoesRepository; // repositório para manipular roles
    private final UsuarioRepository usuarioRepository; // repositório para manipular usuários
    private final PasswordEncoder passwordEncoder; // codificador de senhas

    // Construtor para injetar dependências automaticamente pelo Spring
    public DataInitializer(FuncoesRepository funcoesRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.funcoesRepository = funcoesRepository; // atribui o repositório de funções
        this.usuarioRepository = usuarioRepository; // atribui o repositório de usuários
        this.passwordEncoder = passwordEncoder; // atribui o codificador de senhas
    }

    @Override // sobrescreve o método run de ApplicationRunner
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>> [DataInitializer] INICIANDO A VERIFICAÇÃO/INSERÇÃO DE DADOS INICIAIS...");

        // --- CRIA AS FUNÇÕES (ROLES) ---
        // Busca a role no banco e, se não existir, cria uma nova
        Funcoes adminPrincipalRole = funcoesRepository.findByNome("ADMINISTRADOR_PRINCIPAL").orElseGet(() -> {
            Funcoes newRole = new Funcoes(); // cria nova role
            newRole.setNome("ADMINISTRADOR_PRINCIPAL"); // define o nome da role
            newRole.setDescricao("Pode gerenciar administradores e usuários."); // define descrição da role
            System.out.println(">>> [DataInitializer] Criando role: ADMINISTRADOR_PRINCIPAL");
            return funcoesRepository.save(newRole); // salva a role no banco
        });

        Funcoes adminRole = funcoesRepository.findByNome("ADMINISTRADOR").orElseGet(() -> {
            Funcoes newRole = new Funcoes(); // cria nova role
            newRole.setNome("ADMINISTRADOR"); // define o nome
            newRole.setDescricao("Pode gerenciar apenas usuários padrão."); // define descrição
            System.out.println(">>> [DataInitializer] Criando role: ADMINISTRADOR");
            return funcoesRepository.save(newRole); // salva no banco
        });

        funcoesRepository.findByNome("USUARIO_PADRAO").orElseGet(() -> {
            Funcoes newRole = new Funcoes(); // cria nova role
            newRole.setNome("USUARIO_PADRAO"); // define o nome
            newRole.setDescricao("Acesso básico ao sistema."); // define descrição
            System.out.println(">>> [DataInitializer] Criando role: USUARIO_PADRAO");
            return funcoesRepository.save(newRole); // salva no banco
        });

        // --- CRIA O USUÁRIO ADMIN ---
        // Verifica se o usuário 'Admin' já existe antes de criar
        if (usuarioRepository.findByLogin("Admin").isEmpty()) {
            Usuario adminUser = new Usuario(); // cria novo usuário
            adminUser.setLogin("Admin"); // define login
            adminUser.setSenha(passwordEncoder.encode("Admin123*")); // criptografa a senha
            adminUser.setFuncao(adminPrincipalRole); // atribui role de administrador principal
            
            usuarioRepository.save(adminUser); // salva usuário no banco
            System.out.println(">>> [DataInitializer] Usuário 'Admin' criado com sucesso.");
        } else {
            System.out.println(">>> [DataInitializer] Usuário 'Admin' já existe."); // informa que o usuário já existe
        }

        System.out.println(">>> [DataInitializer] DADOS INICIAIS VERIFICADOS/INSERIDOS. <<<"); // finaliza a inicialização
    }
}