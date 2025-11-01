package com.Imperium.Repositorys; // define o pacote da interface

import java.util.List; // para retornar um valor que pode estar ausente
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; // repositório JPA com métodos prontos

import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Models.Usuario;



// Interface para acessar Usuarios no banco de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
        // Buscar usuário pelo email (para autenticação)
    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    // Se quiser buscar por setor e ativo ao mesmo tempo
    List<Usuario> findByStatusUsuario(StatusUsuario status);
}
