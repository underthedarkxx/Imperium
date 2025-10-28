package com.Imperium.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "setor")
@Entity(name = "setor")
public class Setor {
    @Id
    @Column(name = "idSetor", nullable=false)
    private int idSetor ;
    @Column(name = "nome-setor",nullable=false)
    private String nomeSetor ;
    @Column(name = "ramal-setor", nullable=false)
    private String ramalSetor ;
    @Column(name="descricao",nullable=false)
    private String descricao ;

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public String getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(String nomeSetor) {
        this.nomeSetor = nomeSetor;
    }

    public String getRamalSetor() {
        return ramalSetor;
    }

    public void setRamalSetor(String ramalSetor) {
        this.ramalSetor = ramalSetor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
