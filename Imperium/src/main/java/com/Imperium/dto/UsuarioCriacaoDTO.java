package com.Imperium.dto; // define o pacote da classe

// DTO para receber dados de criação de usuário via requisição
public class UsuarioCriacaoDTO {
    private String login; // login do novo usuário
    private String senha; // senha do novo usuário
    private int funcaoId; // ID da função/role atribuída ao usuário

    // Getter e Setter para login
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    // Getter e Setter para senha
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    // Getter e Setter para funçãoId
    public int getFuncaoId() { return funcaoId; }
    public void setFuncaoId(int funcaoId) { this.funcaoId = funcaoId; }
}
