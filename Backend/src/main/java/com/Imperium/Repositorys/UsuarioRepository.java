package com.Imperium.Repositorys; // define o pacote da interface

import java.util.Optional; // para retornar um valor que pode estar ausente

import org.springframework.data.jpa.repository.JpaRepository; // repositório JPA com métodos prontos

import com.Imperium.Models.Usuario;

// Interface para acessar Usuarios no banco de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca um usuário pelo login, retornando Optional caso não exista
    Optional<Usuario> findByLogin(String login);
}
