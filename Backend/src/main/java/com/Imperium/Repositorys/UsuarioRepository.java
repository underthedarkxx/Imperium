package com.Imperium.Repositorys; // define o pacote da interface

import java.util.List; // para retornar um valor que pode estar ausente
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; // repositório JPA com métodos prontos

import com.Imperium.Enum.Setor;
import com.Imperium.Models.Usuario;



// Interface para acessar Usuarios no banco de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca um usuário pelo login, retornando Optional caso não exista
        // Buscar usuário pelo login (para autenticação)
    Optional<Usuario> findByLogin(String login);

    // Buscar usuário pelo nome do usuario (para evitar duplicação de nome do usuario)
    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    // Buscar usuários por setor
    List<Usuario> findBySetorUsuario(Setor setor);

    // Buscar usuários ativos
    List<Usuario> findByAtivoTrue();

    // Buscar usuário ativo por login
    Optional<Usuario> findByLoginAndAtivoTrue(String login);

    // Se quiser buscar por setor e ativo ao mesmo tempo
    List<Usuario> findBySetorUsuarioAndAtivoTrue(Setor setor);
}
