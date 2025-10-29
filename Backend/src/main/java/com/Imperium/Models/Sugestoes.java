package com.Imperium.Models;

import java.time.LocalDateTime;

import com.Imperium.Enum.statusSugestao;

import jakarta.persistence.Column;
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

@Table(name = "Sugestoes")
@Entity(name = "Sugestoes")
public class Sugestoes{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSugestao")
    private Long id;

    @Column(name = "tituloSugestao", nullable = false, length = 50)
    private String titulo;

    @Column(name = "descricaoSugestao", nullable = false, columnDefinition = "TEXT")
    private String descricaoSugestao;

    @Column(name = "dataEnvioSugestao", nullable = false, updatable = false)
    private LocalDateTime dataEnvioSugestao;

    @Enumerated(EnumType.STRING)
    @Column(name = "statusSugestao", nullable = false)
    private statusSugestao statusSugestao;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idSetor", nullable = false)
    private Setor Setor;

    @PrePersist
    protected void onCreate() {
        this.dataEnvioSugestao = LocalDateTime.now();
    }

    public Long getId(){ return id;}
    public void setId(Long id){ this.id = id;}

    public String getTitulo(){ return titulo;}
    public void setTitulo(String titulo){ this.titulo = titulo;}

    public String getDescricaoSugestoes(){ return descricaoSugestao;}
    public void setDescricaoSugestoes(String descricaoSugestao){ this.descricaoSugestao = descricaoSugestao; }

    public LocalDateTime getDataEnvioSugestao(){ return dataEnvioSugestao;}
    public void setDataEnvioSugestao(LocalDateTime dataEnvioSugestao){ this.dataEnvioSugestao = dataEnvioSugestao;}

    public statusSugestao getStatusSugestao(){ return statusSugestao;}
    public void setStatusSugestao(statusSugestao statusSugestao){ this.statusSugestao = statusSugestao;}

    public Usuario getUsuario(){ return usuario;}
    public void setUsuario(Usuario usuario){ this.usuario = usuario;}

    public Setor getSetor() {
        return this.Setor;
    }

    public void setSetor(Setor Setor) {
        this.Setor = Setor;
    }
}
