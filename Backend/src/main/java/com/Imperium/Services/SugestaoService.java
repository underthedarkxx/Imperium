package com.Imperium.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Imperium.DTOs.SugestaoCriacaoDTO;
import com.Imperium.Enum.statusSugestao; // Importe seu enum
import com.Imperium.Models.Sugestoes;
import com.Imperium.Models.Usuario;
import com.Imperium.Repositorys.SugestoesRepository;

@Service
public class SugestaoService {

    @Autowired
    private SugestoesRepository sugestoesRepository;

    /**
     * Cria uma nova sugestão com base nos dados do DTO e no usuário logado.
     * @param dto O DTO com título e descrição.
     * @param usuarioLogado O usuário que está enviando a sugestão.
     */
    public void criarSugestao(SugestaoCriacaoDTO dto, Usuario usuarioLogado) {
        Sugestoes novaSugestao = new Sugestoes();
        
        // 1. Pega os dados do DTO
        novaSugestao.setTitulo(dto.titulo());
        novaSugestao.setDescricaoSugestoes(dto.descricaoSugestao());
        
        // 2. Define o status inicial (Ex: PENDENTE)
        //    (Assumindo que seu enum 'statusSugestao' tem um valor 'PENDENTE')
        novaSugestao.setStatusSugestao(statusSugestao.Enviada);

        // 3. Pega os dados do usuário que fez a requisição
        novaSugestao.setUsuario(usuarioLogado);
        
        // 4. Pega o setor do usuário (conforme sua entidade Sugestoes pede)
        novaSugestao.setSetor(usuarioLogado.getSetor());

        // 5. Salva no banco
        //    (A data de envio será preenchida pelo @PrePersist)
        sugestoesRepository.save(novaSugestao);
    }
}