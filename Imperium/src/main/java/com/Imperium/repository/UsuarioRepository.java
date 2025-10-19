package com.Imperium.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Imperium.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}
