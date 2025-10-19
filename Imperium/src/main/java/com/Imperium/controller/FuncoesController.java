package com.Imperium.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Imperium.model.Funcoes;
import com.Imperium.repository.FuncoesRepository;


@RestController
@RequestMapping("/api/funcoes")
public class FuncoesController {
    @Autowired
    private FuncoesRepository funcoesRepository;

    @GetMapping
    public ResponseEntity<List<Funcoes>> listarFuncoes(){
        List<Funcoes> funcoes = funcoesRepository.findAll();
        return ResponseEntity.ok(funcoes);
    }
    
}
