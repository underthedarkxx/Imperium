package com.Imperium.dto; // define o pacote da classe

// DTO para receber dados de atualização de usuário via requisição
public class UsuarioUpdateDTO {
    private String senha; // nova senha do usuário (opcional)
    private Integer funcaoId; // novo ID da função/role do usuário (opcional)

    // Getter e Setter para senha
    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    // Getter e Setter para funcaoId
    public Integer getFuncaoId() {
        return funcaoId;
    }

    public void setFuncaoId(Integer funcaoId) {
        this.funcaoId = funcaoId;
    }
}