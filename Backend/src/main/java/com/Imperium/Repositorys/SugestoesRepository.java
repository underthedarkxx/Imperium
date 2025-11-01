package com.Imperium.Repositorys;

import com.Imperium.Models.Sugestoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SugestoesRepository extends JpaRepository<Sugestoes, Long> {
    // Você pode adicionar métodos de busca aqui depois, 
    // como findByStatusSugestao(statusSugestao status)
}
