package com.Imperium.model; // define o pacote da classe

import java.time.LocalDateTime; // para armazenar datas

import jakarta.persistence.Column; // coleção de autoridades do usuário
import jakarta.persistence.Entity; // listas
import jakarta.persistence.FetchType; // interface para permissões de usuário
import jakarta.persistence.GeneratedValue; // implementação de GrantedAuthority
import jakarta.persistence.GenerationType; // mapeamento de colunas
import jakarta.persistence.Id; // marca classe como entidade JPA
import jakarta.persistence.JoinColumn; // define tipo de fetch para relacionamento
import jakarta.persistence.ManyToOne; // define geração automática de ID
import jakarta.persistence.PrePersist; // tipo de geração de ID
import jakarta.persistence.Table; // marca chave primária

@Entity // indica que esta classe é uma entidade JPA
@Table(name = "usuarios") // nome da tabela no banco de dados
public class Usuario { // implementa UserDetails para integração com Spring Security

    @Id // chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincrement
    private Long id;

    @Column(name = "login", nullable = false, length = 255) // coluna login
    private String login;

    @Column(name = "senha", nullable = false, length = 255) // coluna senha
    private String senha;

    @Column(name = "data_cadastro", nullable = false) // data de cadastro
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultimo_acesso") // data do último acesso
    private LocalDateTime dataUltimoAcesso;

    private boolean ativo; // indica se usuário está ativo

    @ManyToOne(fetch = FetchType.EAGER) // relacionamento com Funcoes, sempre busca função junto
    @JoinColumn(name = "id_funcao") // coluna de junção para função
    private Funcoes funcao;

    /*
    // Implementações do UserDetails para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.funcao == null) {
            return List.of(); // sem autoridade se não houver função
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + funcao.getNome().toUpperCase())); // cria autoridade com prefixo ROLE_
    }*/

    public String getPassword(){
        return this.senha;
    }

    public String getUsername(){
        return this.login;
    }

    public boolean isAccountNonExpired(){
        return true; // conta nunca expira
    }
    
    public boolean isAccountNonLocked(){
        return true; // conta nunca bloqueia
    }

    public boolean isCredentialsNonExpired(){
        return true; // credenciais nunca expiram
    }

    public boolean isEnabled(){
        return true; // conta sempre habilitada (ativo é verificado separadamente)
    }

    // Getters e Setters
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getLogin(){ return login; }
    public void setLogin(String login){ this.login = login; }

    public String getSenha(){ return senha; }
    public void setSenha(String senha){ this.senha = senha; }

    public LocalDateTime getDataCadastro(){ return dataCadastro; }

    public LocalDateTime getDataUltimoAcesso(){ return dataUltimoAcesso; }
    public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso){ this.dataUltimoAcesso = dataUltimoAcesso; }

    public Funcoes getFuncao() { return funcao; }
    public void setFuncao(Funcoes funcao) { this.funcao = funcao; }

    @PrePersist // executa antes de persistir no banco
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now(); // define data de cadastro atual
        this.ativo = true; // define usuário como ativo por padrão
    }
}
