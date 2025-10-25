package com.Imperium.DTOs; // define o pacote da classe

// DTO para receber dados de atualização de usuário via requisição
import com.Imperium.Enum.Setor;


public class UsuarioUpdateDTO {
    private String senha; // nova senha do usuário (opcional)
    private Setor setorUsuario; // novo ID da função/role do usuário (opcional)

    // Getter e Setter para senha
    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    // Getter e Setter para setorUsuario
    public Setor getSetorUsuario() {
        return setorUsuario;
    }

    public void setSetorUsuario(Setor setorUsuario) {
        this.setorUsuario = setorUsuario;
    }
}