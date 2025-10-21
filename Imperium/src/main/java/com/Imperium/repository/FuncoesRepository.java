package com.Imperium.repository; // define o pacote da interface

import java.util.Optional; // para retornar um valor que pode estar ausente

import org.springframework.data.jpa.repository.JpaRepository; // repositório JPA com métodos prontos

import com.Imperium.model.Funcoes; // modelo Funcoes

// Interface para acessar Funcoes no banco de dados
public interface FuncoesRepository extends JpaRepository<Funcoes, Integer> {
    // Busca uma função pelo nome, retornando Optional caso não exista
    Optional<Funcoes> findByNome(String nome);
}