package com.Imperium.Models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importe a interface UserDetails

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { // <-- 1. Adicione "implements UserDetails"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true, length = 255) // Adicione unique = true
    private String login;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultimo_acesso")
    private LocalDateTime dataUltimoAcesso;

    private boolean ativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_funcao")
    private Funcoes funcao;

    // --- MÉTODOS DO USERDETAILS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // <-- 2. Descomente este método. Ele informa ao Spring qual é a "ROLE" do usuário.
        if (this.funcao == null) {
            return List.of(); // Nenhuma permissão se não houver função
        }
        // O nome da função será a permissão. Ex: "ADMINISTRADOR_PRINCIPAL"
        return List.of(new SimpleGrantedAuthority("ROLE_" + funcao.getNome().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        // <-- 3. Conecte com o campo 'ativo'. Agora desativar um usuário vai impedi-lo de logar.
        return this.ativo; 
    }

    // --- GETTERS E SETTERS ---
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }


    public LocalDateTime getDataUltimoAcesso() { return dataUltimoAcesso; }
    public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso) { this.dataUltimoAcesso = dataUltimoAcesso; }

    public Funcoes getFuncao() { return funcao; }
    public void setFuncao(Funcoes funcao) { this.funcao = funcao; }

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
}
