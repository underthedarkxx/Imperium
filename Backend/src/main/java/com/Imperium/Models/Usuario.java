package com.Imperium.Models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Imperium.Enum.papelUsuario;

import jakarta.persistence.Column; // Importe a interface UserDetails
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long id;

    @Column(name= "emailUsuario", nullable = false, length = 100)
    private String emailUsuario;

    @Column(name = "senhaUsuario", nullable = false, length = 255)
    private String senhaUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "papelUsuario", nullable = false)
    private papelUsuario papelUsuario;

    @Column(name = "dataInicioUsuario", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "ultimaAlteracaoUsuario")
    private LocalDateTime dataUltimoAcesso;

    @ManyToOne
    @JoinColumn(name = "idSetor")
    private Setor setor;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + papelUsuario.name()));
    }


    @Override
    public String getPassword() {
        return this.senhaUsuario;
    }

    @Override
    public String getUsername() {
        return this.emailUsuario;
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

    // --- GETTERS E SETTERS ---
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmailUsuario(){ return emailUsuario;}
    public void setNomeUsuario( String emailUsuario){this.emailUsuario = emailUsuario;}

    public String getSenhaUsuario() { return senhaUsuario; }
    public void setSenhaUsuario(String senhaUsuario) { this.senhaUsuario = senhaUsuario; }

    public papelUsuario getPapelUsuario() { return papelUsuario; }
    public void setPapelUsuario(papelUsuario papelUsuario) { this.papelUsuario = papelUsuario; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDateTime getDataUltimoAcesso() { return dataUltimoAcesso; }
    public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso) { this.dataUltimoAcesso = dataUltimoAcesso; }

    public Setor getIdSetor(){return setor;}
    public void setIdSetor(Setor setor){ this.setor = setor;}

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }
}
