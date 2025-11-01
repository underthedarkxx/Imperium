package com.Imperium.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Imperium.Models.Setor;

// O <Setor, Integer> significa que ele gerencia a entidade "Setor"
// e o ID dessa entidade é do tipo "Integer" (int)
public interface SetorRepository extends JpaRepository<Setor, Integer> {
    Optional<Setor> findByNomeSetor(String nomeSetor);
}
