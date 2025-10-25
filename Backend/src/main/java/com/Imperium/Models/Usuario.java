package com.Imperium.Models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importe a interface UserDetails

import com.Imperium.Enum.Setor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "nomeUsuario", nullable = false, unique = true, length = 30)
    private String nomeUsuario;

    @Column(name = "login", nullable = false, unique = true, length = 255) // Adicione unique = true
    private String login;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    private boolean ativo;

    @Column(name = "Criacao", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultimo_acesso")
    private LocalDateTime dataUltimoAcesso;

    @Enumerated(EnumType.STRING)
    @Column(name ="setorUsuario", nullable = false)
    private Setor setorUsuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + setorUsuario.name()));
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
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeUsuario(){ return nomeUsuario;}
    public void setNomeUsuario( String nomeUsuario){this.nomeUsuario = nomeUsuario;}

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDateTime getDataUltimoAcesso() { return dataUltimoAcesso; }
    public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso) { this.dataUltimoAcesso = dataUltimoAcesso; }

    public Setor getSetorUsuario(){ return setorUsuario;}
    public void setSetorUsuario(Setor setorUsuario){
        this.setorUsuario = setorUsuario;
    }

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
}
