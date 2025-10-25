package com.Imperium.DTOs; // define o pacote da classe

import com.Imperium.Enum.Setor;
// DTO para receber dados de criação de usuário via requisição
public class UsuarioCriacaoDTO {
    private String nomeUsuario;
    private String login; // login do novo usuário
    private String senha; // senha do novo usuário
    private Setor setorUsuario; // ID da função/role atribuída ao usuário

    // Getter e Setter para login
    public String getNomeUsuario(){ return nomeUsuario;}
    public void setNomeUsuario(String nomeUsuario){this.nomeUsuario = nomeUsuario;}

    // Getter e Setter para login
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    // Getter e Setter para senha
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    // Getter e Setter para funçãoId
    public Setor getSetorUsuario() { return setorUsuario; }
    public void setSetorUsuario(Setor setorUsuario) { this.setorUsuario = setorUsuario; }
}
