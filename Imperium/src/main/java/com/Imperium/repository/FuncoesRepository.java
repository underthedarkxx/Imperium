package com.Imperium.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Imperium.model.Funcoes;

public interface FuncoesRepository extends JpaRepository<Funcoes, Integer> {
    Optional<Funcoes> findByNome(String nome);
}
