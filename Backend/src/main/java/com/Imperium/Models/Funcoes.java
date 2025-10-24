package com.Imperium.Models; // define o pacote da classe

import jakarta.persistence.Column; // anotações para mapeamento de colunas no banco
import jakarta.persistence.Entity; // marca a classe como entidade JPA
import jakarta.persistence.GeneratedValue; // define geração automática de ID
import jakarta.persistence.GenerationType; // tipo de geração de ID
import jakarta.persistence.Id; // marca o campo como chave primária
import jakarta.persistence.Table; // define o nome da tabela no banco

@Entity // indica que esta classe é uma entidade JPA
@Table(name = "funcoes") // define o nome da tabela no banco de dados
public class Funcoes{

    @Id // identifica a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // gera ID automaticamente com auto incremento
    @Column(name = "id_funcao") // mapeia a coluna no banco
    private int idFuncao;

    @Column(name = "nome", nullable = false, length = 150) // define coluna obrigatória com tamanho máximo
    private String nome;

    @Column(name = "descricao") // define coluna para descrição (opcional)
    private String descricao;

    // Getter e Setter para idFuncao
    public int getIdFuncao(){
        return idFuncao;
    }

    public void setIdFuncao(int idFuncao){
        this.idFuncao = idFuncao;
    }

    // Getter e Setter para nome
    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    // Getter e Setter para descricao
    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
}
