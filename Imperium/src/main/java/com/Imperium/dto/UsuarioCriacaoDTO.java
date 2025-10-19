package com.Imperium.dto;

public class UsuarioCriacaoDTO {
    private String login;
    private String senha;
    private int funcaoId;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public int getFuncaoId() { return funcaoId; }
    public void setFuncaoId(int funcaoId) { this.funcaoId = funcaoId; }
}
