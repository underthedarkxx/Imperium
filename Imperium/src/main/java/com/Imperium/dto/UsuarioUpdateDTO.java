package com.Imperium.dto;

public class UsuarioUpdateDTO {
    private String senha;
    private Integer funcaoId;

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public Integer getFuncaoId() {
        return funcaoId;
    }

    public void setFuncaoId(Integer funcaoId) {
        this.funcaoId = funcaoId;
    }
}
