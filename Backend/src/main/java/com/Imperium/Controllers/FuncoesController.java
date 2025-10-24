package com.Imperium.Controllers; // define o pacote da classe

import java.util.List; // para trabalhar com listas de funções

import org.springframework.beans.factory.annotation.Autowired; // permite injeção automática de dependências
import org.springframework.http.ResponseEntity; // para retornar respostas HTTP
import org.springframework.web.bind.annotation.GetMapping; // mapeia requisições GET
import org.springframework.web.bind.annotation.RequestMapping; // define caminho base da API
import org.springframework.web.bind.annotation.RestController; // indica que é um controller REST

import com.Imperium.Models.Funcoes;
import com.Imperium.Repositorys.FuncoesRepository;

@RestController // marca a classe como REST controller
@RequestMapping("/api/funcoes") // define caminho base para os endpoints desta classe
public class FuncoesController {

    @Autowired // injeta automaticamente o repositório de funções
    private FuncoesRepository funcoesRepository;

    @GetMapping // mapeia requisições GET para listar funções
    public ResponseEntity<List<Funcoes>> listarFuncoes(){
        List<Funcoes> funcoes = funcoesRepository.findAll(); // busca todas as funções no banco
        return ResponseEntity.ok(funcoes); // retorna status 200 OK com a lista de funções
    }
}
